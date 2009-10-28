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

/*
 * An instance of this class represents an entry of a 
 * HTTP post message.
 */
@interface KHttpPostMessageContent :NSObject 
{
    NSData *data;
    NSDictionary *headers;
}

@property(nonatomic, retain) NSDictionary *headers;
@property(nonatomic, retain) NSData *data;

/*
 Creates a new content with a string as data
 */
- (id) initWithString:(NSString *)_string;

/*
 Creates a new content instance with the given data
 */
- (id) initWithData:(NSData *)_data;

/*
 Sets the content type of this content.
 Default is set to multipart/form-data
 */
- (void) setContentType:(NSString *)_type;

/*
 Sets the content disposition of this content.
 */
- (void) setContentDisposition:(NSString *)_disposition;

/*
 Sets the content length of this content
 */
- (void) setContentLength:(int)_length;

/*
 Adds a custom header
 */
- (void) addHeader:(id)_value forKey:(NSString *)_key;

/*
 Returns the content as a string.
 */
- (NSString *) contentAsString;

/*
 eturns this content as string to be placed within a post message
 */
- (NSData *) contentAsData;

@end
