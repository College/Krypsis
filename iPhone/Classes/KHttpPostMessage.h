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
 This HTTP message is used to create an envelope to collect
 post parameters. You can add an unspecified amount of string
 parameters and one file parameter.
 */
@interface KHttpPostMessage :NSObject 
{
    NSString        *boundary;
    NSDictionary    *parameters;
    NSDictionary    *props;
    NSURL           *url;
}

@property(nonatomic, copy)   NSString       *boundary;
@property(nonatomic, retain) NSDictionary   *parameters;
@property(nonatomic, retain) NSDictionary   *props;
@property(nonatomic, retain) NSURL          *url;

/*
 Creates a new Http message with the given url
 as server target address.
 */
- (id) initWithUrl:(NSURL *)_url;

/*
 Adds the given parameter to the message
 */
- (void) addValue:(NSString *)_value forName:(NSString *)_name;

/*
 Adds file content to the message
 */
- (void) addFileData:(NSData *)_data forName:(NSString *)_name fileName:(NSString *)_fileName mimeType:(NSString *)_mimeType;

/*
 Returns the contents of this message as data.
 */
- (NSData *) contentAsData;

@end
