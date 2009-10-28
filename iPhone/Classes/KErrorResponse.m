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

#import "KErrorResponse.h"

@implementation KErrorResponse
@synthesize code;

- (id)initWithRequest:(KRequest *)_request andWithCode:(int)_code
{
    if (self = [super initWithRequest:_request])
    {
        self.code = _code;
        [self.parameters setValue:[NSNumber numberWithInt:_code] forKey:@"code"];
    }
    return self;
}


- (NSString*) javaScriptResultType
{
    return @"Error";
}

@end
