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

#import "KHttpPostMessage.h"
#import "KHttpPostMessageContent.h"
#import "KHttpPostMessageFileContent.h"
#import "KHttpPostMessageFormContent.h"
#import "KStringUtils.h"

@implementation KHttpPostMessage

@synthesize parameters;
@synthesize props;
@synthesize boundary;
@synthesize url;

- (id) initWithUrl:(NSURL *)_url 
{
    if (self = [super init]) 
    {
        self.url        = _url;
        self.props      = [[NSMutableDictionary alloc] init];
        self.parameters = [[NSMutableDictionary alloc] init];
        
        self.boundary = [KStringUtils generateStringWithLenghtOf:24];
        [self.props setValue:[NSString stringWithFormat:@"multipart/form-data; boundary=%@; charset=\"UTF-8\"", self.boundary] forKey:@"Content-Type"];
    }
    
    return self;
}

- (void) addValue:(NSString *)_value forName:(NSString *)_name 
{
    KHttpPostMessageContent *content = [[KHttpPostMessageFormContent alloc] initWithData:_value forName:_name];
    [self.parameters setValue:content forKey:_name];
    [content release];
}

- (void) addFileData:(NSData *)_data forName:(NSString *)_name fileName:(NSString *)_fileName mimeType:(NSString *)_mimeType 
{
    KHttpPostMessageContent *file = [[KHttpPostMessageFileContent alloc] initWithContentType:_mimeType name:_name fileName:_fileName data:_data];
    [self.parameters setValue:file forKey:_name];
    [file release];
}

- (NSData *) contentAsData {
    NSMutableData *buffer = [[[NSMutableData alloc] init] autorelease];
    if ([self.parameters count] > 0) 
    {
        for (NSString *contentKey in parameters) 
        {
            KHttpPostMessageContent *content = [parameters valueForKey:contentKey];
            [buffer appendData:[[NSString stringWithFormat:@"--%@", self.boundary] dataUsingEncoding:NSISOLatin1StringEncoding]];
            [buffer appendData:[@"\r\n" dataUsingEncoding:NSISOLatin1StringEncoding]];
            [buffer appendData:[content contentAsData]];
            [buffer appendData:[@"\r\n" dataUsingEncoding:NSISOLatin1StringEncoding]];
        }
    }
    
    [buffer appendData:[[NSString stringWithFormat:@"--%@--", self.boundary] dataUsingEncoding:NSISOLatin1StringEncoding]];
    
    return buffer;
}

- (void) dealloc 
{
    [parameters release];
    [props      release];
    [url        release];
    self.boundary = nil;
    
    [super dealloc];
}

@end
