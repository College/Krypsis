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

#import "KScreenGetResolutionCommand.h"
#import "KSuccessResponse.h"

@implementation KScreenGetResolutionCommand

- (void) executeWithDelegate:(KrypsisAppDelegate *) appDelegate andRequest:(KRequest *) request 
{
    UIScreen *screen    = [UIScreen mainScreen];
    CGRect rect         = [screen bounds];
    KResponse *response = [[KSuccessResponse alloc] initWithRequest:request];
    
    UIDeviceOrientation orientation = [[UIDevice currentDevice] orientation];
    
    if ((orientation == UIDeviceOrientationLandscapeLeft || orientation == UIDeviceOrientationLandscapeRight)) 
    {
        [response.parameters setValue:[NSNumber numberWithInt:90] forKey:@"orientation"];
    }
    else 
    {
        [response.parameters setValue:[NSNumber numberWithInt:0] forKey:@"orientation"];
    }
    
    [response.parameters setValue:[NSNumber numberWithInt:rect.size.width]  forKey:@"width"];
    [response.parameters setValue:[NSNumber numberWithInt:rect.size.height] forKey:@"height"];
    
    [appDelegate dispatchResponse:response];
    [response release];
}

@end
