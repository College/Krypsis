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

@interface KRequest :NSObject
{
	NSString *moduleName;
	NSString *actionName;
	NSDictionary *parameters;
}

@property (nonatomic, copy) NSString *moduleName;
@property (nonatomic, copy) NSString *actionName;
@property (nonatomic, retain) NSDictionary *parameters;

- (void) parseUrl:(NSURL *)_url;

/*
 Removes the Krypsis namespace from the full path string.
 Example:If the path /org/krypsis/device/base/info is given
 then the /org/krypsis/device/ string is removed and the trailing
 string is returned:base/info
 Notice:The result does not have a leading slash ('/')
 */
- (NSString *) removeNamespaceFromPath:(NSString *)_path;

/*
 Extracts the parameters key-value pairs from the given string
 and returns them as a dictionary
 */
- (NSDictionary *) parametersFromString:(NSString*)_parameterString;

@end
