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
#import "KGlobals.h"
#import "KrypsisAppDelegate.h"
#import "KRequest.h"


@interface KOrientationListener :NSObject 
{
    KrypsisAppDelegate *appDelegate;
    KRequest *request;
}

@property(nonatomic, retain) KrypsisAppDelegate *appDelegate;
@property(nonatomic, retain) KRequest *request;

/*
 Creates a new orientation listener with the given application and request
 To start this listener call start()
 */
- (id) initWithAppDelegate:(KrypsisAppDelegate *)_appDelegate andRequest:(KRequest *)_request;

/*
 Will be executed whenever the orientation has changed.
 */
- (void) orientationDidChanged:(NSNotification *)_notification;

/*
 Start this listener
 */
- (void) start;

/*
 Stop this listener
 */
- (void) stop;

@end
