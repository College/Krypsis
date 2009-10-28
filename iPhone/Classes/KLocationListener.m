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

#import "KLocationListener.h"
#import "KResponse.h"
#import "KSuccessResponse.h"
#import "KErrorResponse.h"
#import "KErrors.h"
#import "KGlobals.h"

@implementation KLocationListener
@synthesize request;
@synthesize appDelegate;

- (id) initWithAppDelegate:(KrypsisAppDelegate *)_appDelegate andRequest:(KRequest *)_request 
{
    if (self = [super init]) 
    {
        self.appDelegate = _appDelegate;
        self.request = _request;
    }
    return self;
}

- (void) start 
{
    locationManager = [[CLLocationManager alloc] init];
    locationManager.delegate = self;
    locationManager.distanceFilter = kCLDistanceFilterNone;
    locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    [locationManager startUpdatingLocation];
}

- (void) stop 
{
    [locationManager stopUpdatingLocation];
    locationManager.delegate = nil;
    [locationManager autorelease];
}

- (void)locationManager:(CLLocationManager *) manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *) oldLocation 
{
    if (newLocation) 
    {
        NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
        KResponse *response = [[[KSuccessResponse alloc] initWithRequest:request] autorelease];
        
        NSString *millis = [NSString stringWithFormat:@"%0.3f", [newLocation.timestamp timeIntervalSince1970]];
        millis = [millis stringByReplacingOccurrencesOfString:@"." withString:@""];
        
        [response.parameters setValue:millis forKey:@"timestamp"];
        [response.parameters setValue:[NSNumber numberWithDouble:newLocation.coordinate.latitude] forKey:@"latitude"];
        [response.parameters setValue:[NSNumber numberWithDouble:newLocation.coordinate.longitude] forKey:@"longitude"];
        [response.parameters setValue:[NSNumber numberWithDouble:newLocation.altitude] forKey:@"altitude"];
        
        [self.appDelegate dispatchResponse:response];
        
        [pool release];
    }
}

- (void)locationManager:(CLLocationManager *) manager didFailWithError:(NSError *) error 
{
    
    NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
    
    BOOL destroySelf = NO;
    KResponse *response = nil;
    KRequest *requestCopy = [[[KRequest alloc] initWithRequest:self.request] autorelease];
    
    switch ([error code]) 
    {
        case kCLErrorDenied :
            response = [[[KErrorResponse alloc] initWithRequest:requestCopy andWithCode:ERROR_UNKNOWN] autorelease];
            destroySelf = YES;
            [self stop];
            break;
        case kCLErrorNetwork :
            response = [[[KErrorResponse alloc] initWithRequest:requestCopy andWithCode:ERROR_NETWORK] autorelease];
            break;
        default:
            NSLog(@"Error from locationmanager:%@", [error code]);
            break;
    }
    
    if (response) 
    {
        [self.appDelegate dispatchResponse:response];
    }
    
    [pool release];
    
    if (destroySelf) 
    {
        [self destroyListener];
    }
}

- (void) destroyListener 
{
    [self.appDelegate removeValueFromScope:LOCATION_LISTENER];
}

- (void) dealloc 
{
    [appDelegate release];
    [request release];
    
    [super dealloc];
}

@end
