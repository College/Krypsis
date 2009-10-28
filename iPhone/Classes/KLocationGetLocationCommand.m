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

#import "KLocationGetLocationCommand.h"
#import "KOneTimeLocationListener.h"
#import "KGlobals.h"
#import "KErrors.h"

@implementation KLocationGetLocationCommand

- (void) executeWithDelegate:(KrypsisAppDelegate *)appDelegate andRequest:(KRequest *)request 
{
    KLocationListener *listener = [appDelegate valueFromScopeForKey:ONE_TIME_LOCATION_LISTENER];
    
    if (listener) 
    {
        @throw [NSException exceptionWithName:@"Request Exception" 
                                       reason:@"Listener already started" 
                                     userInfo:[NSDictionary dictionaryWithObject:[NSNumber numberWithInt:ERROR_OBSERVER_STARTED] 
                                                                          forKey:@"code"]];
    }
    
    listener = [[KOneTimeLocationListener alloc] initWithAppDelegate:appDelegate andRequest:request];
    [appDelegate putValueInScope:listener forKey:ONE_TIME_LOCATION_LISTENER];
    
    [listener start];
    [listener autorelease];
}

@end
