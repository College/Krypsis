/*
 Krypsis - Write web applications based on proved technologies
 like HTML, JavaScript, CSS... and access functionality of your
 smartphone in a platform independent way.
 
 Copyright (C) 2008 - 2009 krypsis.org (have.a.go@krypsis.org) 
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

#import "KPhotoPickerAndUploader.h"
#import "KImageUtils.h"
#import "KErrors.h"
#import "KSuccessResponse.h"
#import "KErrorResponse.h"
#import "KPhotoUploader.h"

@implementation KPhotoPickerAndUploader

@synthesize request;
@synthesize appDelegate;
@synthesize connection;
@synthesize httpMessage;
@synthesize photoPicker;

@synthesize fileName;
@synthesize parameterName;

- (id) initWithAppDelegate:(KrypsisAppDelegate *)_appDelegate request:(KRequest *)_request {
    if (self = [super init]) {
        [self setAppDelegate:_appDelegate];
        [self setRequest:_request];
    }
    return self;
}

- (void) pickAndUpload {
    self.photoPicker = [[KPhotoPicker alloc] initWithPickerProtocol:self];
    [photoPicker pickWithParentController:[appDelegate navigationController]];
}

- (void) didFinishPickingImage:(UIImage *)_image {
    /* Scale the image down to the maximum width/height of 1024 px */
    _image = [KImageUtils scaleImage:_image downToRect:CGRectMake(0, 0, 1024, 1024)];
    NSData *bytes = UIImageJPEGRepresentation(_image, 0.9);
    
    if (bytes) {
        [self uploadImageData:bytes];
    }
    else {
        KResponse *response = [[KErrorResponse alloc] initWithRequest:self.request andWithCode:ERROR_NETWORK];
        [self.appDelegate dispatchResponse:response];
        [response release];
    }
}

- (void) didCancelPickingImage {
    KResponse *response = [[KErrorResponse alloc] initWithRequest:self.request andWithCode:ERROR_CANCELED_BY_USER];
    [self.appDelegate dispatchResponse:response];
    [response release];
    
    [self autorelease];
}

- (void) uploadImageData:(NSData *)_data {
    [httpMessage addFileData:_data forName:self.parameterName fileName:self.fileName mimeType:@"image/jpeg"];
    KPhotoUploader *uploader = [[KPhotoUploader alloc] initWithMessage:httpMessage];
    self.connection = [uploader upload:self];

    [uploader autorelease];
    
    [self autorelease];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    NSString *responseString = @"";
    
    if (data) {
        responseString = [[[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding] autorelease];
    }    
    
    KResponse *response = [[[KSuccessResponse alloc] initWithRequest:self.request] autorelease];
    [response.parameters setValue:responseString forKey:@"response"];
    [appDelegate dispatchResponse:response];
}

- (void) dealloc {
    self.fileName = nil;
    self.parameterName = nil;
    
    [photoPicker release];
    [request release];
    [appDelegate release];
    [connection release];
    [httpMessage release];
    
    [super dealloc];
}
@end
