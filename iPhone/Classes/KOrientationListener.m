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

#import "KOrientationListener.h"
#import "KGlobals.h"
#import "KSuccessResponse.h"

@implementation KOrientationListener
@synthesize appDelegate;
@synthesize request;

- (id) initWithAppDelegate:(KrypsisAppDelegate *)_appDelegate andRequest:(KRequest *)_request
{
    if (self = [super init])
    {
        self.appDelegate = _appDelegate;
        self.request = _request;
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationWillTerminate:) name:@"UIApplicationWillTerminateNotification" object:nil];
    }
    return self;
}

- (void) start
{
    [[UIDevice currentDevice] beginGeneratingDeviceOrientationNotifications];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(orientationDidChanged:) name:@"UIDeviceOrientationDidChangeNotification" object:nil];    
}

- (void) stop
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"UIDeviceOrientationDidChangeNotification" object:nil];
    [[UIDevice currentDevice] endGeneratingDeviceOrientationNotifications];
}

- (void) applicationWillTerminate
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"UIApplicationWillTerminateNotification" object:nil];
    [self.appDelegate setValue:nil forKey:ORIENTATION_LISTENER];
    [self stop];
}

- (void) orientationDidChanged:(NSNotification *)_notification
{
    NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
    
    @try 
    {
        KResponse *response = [[[KSuccessResponse alloc] initWithRequest:self.request] autorelease];
        UIDeviceOrientation orientation = [[UIDevice currentDevice] orientation];
        
        if ((orientation == UIDeviceOrientationLandscapeLeft || orientation == UIDeviceOrientationLandscapeRight)) 
        {
            [response.parameters setValue:[NSNumber numberWithInt:90] forKey:@"orientation"];
        }
        else 
        {
            [response.parameters setValue:[NSNumber numberWithInt:0] forKey:@"orientation"];
        }
        
        [self.appDelegate dispatchResponse:response];
    }
    @catch (NSException * e) 
    {
        NSLog(@"Could not notify the new orientation:%@", [e description]);
    }
    @finally 
    {
        [pool release];
    }
   
}

- (void) dealloc
{   
    self.appDelegate = nil;
    [request release];
    
    [super dealloc];
}

@end
