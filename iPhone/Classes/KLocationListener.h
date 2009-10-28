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

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import <Foundation/Foundation.h>
#import "KRequest.h"
#import "KrypsisAppDelegate.h"

@interface KLocationListener : NSObject <CLLocationManagerDelegate> 
{
    CLLocationManager *locationManager;
    KRequest *request;
    KrypsisAppDelegate *appDelegate;
}

@property (nonatomic, retain) KRequest *request;
@property (nonatomic, retain) KrypsisAppDelegate *appDelegate;

/*
 Initializes the location listener with the application
 and the request. Notice:Don't forget to start the listener
 to receive location updates.
 */
- (id) initWithAppDelegate:(KrypsisAppDelegate *) _appDelegate andRequest:(KRequest *) _request;

/*
 Start the location listener.
 At this time a new instance of the location manager
 is created and the listener will do his work by
 delivering the locations
 */
- (void) start;

/*
 Stops this listener by deactivating the deliver of
 update location events of the responsible location manager
 */
- (void) stop;

/*
 Destroy this listener
 */
- (void) destroyListener;


@end
