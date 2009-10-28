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

#import "KScreenStartOrientationListenerCommand.h"
#import "KGlobals.h"
#import "KErrors.h"
#import "KOrientationListener.h"

@implementation KScreenStartOrientationListenerCommand

- (void) executeWithDelegate:(KrypsisAppDelegate *) appDelegate andRequest:(KRequest *) request;
{
    KOrientationListener *listener = [appDelegate valueFromScopeForKey:ORIENTATION_LISTENER];

    /* Don't start the observer a second time. */
    if (listener) 
    {
        @throw [NSException exceptionWithName:@"Request exception" 
                                       reason:@"Listener already exists" 
                                     userInfo:[NSDictionary dictionaryWithObject:[NSNumber numberWithInt:ERROR_OBSERVER_STARTED] forKey:@"code"]];
    }
    
    listener = [[KOrientationListener alloc] initWithAppDelegate:appDelegate andRequest:request];
    [appDelegate putValueInScope:listener forKey:ORIENTATION_LISTENER];
    
    [listener start];
    [listener autorelease];
}

@end
