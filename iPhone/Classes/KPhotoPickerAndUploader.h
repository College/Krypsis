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

#import <Foundation/Foundation.h>
#import "KrypsisAppDelegate.h"
#import "KRequest.h"
#import "KPhotoPickerProtocol.h"
#import "KPhotoPicker.h"
#import "KHttpPostMessage.h"

@interface KPhotoPickerAndUploader :NSObject <KPhotoPickerProtocol> {
    KrypsisAppDelegate *appDelegate;
    KRequest *request;
    NSURLConnection *connection;
    
    NSString *fileName;
    NSString *parameterName;
    KHttpPostMessage *httpMessage;
    KPhotoPicker *photoPicker;
}

@property (nonatomic, retain) KrypsisAppDelegate *appDelegate;
@property (nonatomic, retain) KRequest *request;
@property (nonatomic, retain) NSURLConnection *connection;
@property (nonatomic, retain) KHttpPostMessage *httpMessage;
@property (nonatomic, retain) KPhotoPicker *photoPicker;

@property (nonatomic, copy) NSString *fileName;
@property (nonatomic, copy) NSString *parameterName;

/**
 * Creates a new photo picker and uploader.
 * The task of this class is to pick a picture
 * (from camera or album) and upload it to a remote server.
 */
- (id) initWithAppDelegate:(KrypsisAppDelegate *)_appDelegate request:(KRequest *)_request;

/**
 * Start doing the work. First pick the picture (from camera or library)
 * and then upload it to the remote server.
 */
- (void) pickAndUpload;

/**
 * Uploads the given image data to the upload url.
 * When finshes this uplaoded will destroy itself.
 */
- (void) uploadImageData:(NSData *)_data;

@end
