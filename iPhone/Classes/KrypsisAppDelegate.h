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
#import "KRequestDispatchProtocol.h"
#import "KResponse.h"

@class KrypsisWebViewController;

@interface KrypsisAppDelegate :NSObject <UIApplicationDelegate, KRequestDispatchProtocol> {
    UIWindow *window;
    KrypsisWebViewController *webViewController;
    UINavigationController *navigationController;
    NSMutableDictionary *modules;
    NSMutableDictionary *scope;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet KrypsisWebViewController *webViewController;
@property (nonatomic, retain) IBOutlet UINavigationController *navigationController;
@property (nonatomic, retain) NSMutableDictionary *modules;
@property (nonatomic, retain) NSMutableDictionary *scope;

/**
 * Returns the unique Krypsis ID for the current device.
 * This ID will be generated when the Krypsis application
 * starts the very first time. Then the ID is stored and
 * loaded each time the Krypsis App is started.
 */
- (NSString *) krypsisId;

/**
 * Dispatches the given response to the web view
 * controller. The controller expects a JavaScript call
 * and will force the browser to execute this JavaScript call.
 */
- (void) dispatchResponse:(KResponse *) response;

/**
 * Registeres the given command class with the module and action
 * name. You can receive this class by using the commandClassForModule
 * function.
 */
- (void) registerCommandClass:(id) clazz forModule:(NSString *) moduleName andAction:(NSString *) actionName;

/**
 * Returns the command class that is associated with the
 * module and action name.
 * If command was not found, nil is returned
 */
- (id) commandClassForModule:(NSString *) moduleName andAction:(NSString *) actionName;

/**
 * Put a value into the application scope.
 * This can be used to share data between command instances
 */
- (void) putValueInScope:(NSObject *) value forKey:(NSString *) key;

/**
 * Receive value from scope that is associates with
 * the given key. If no such value found, nil is returned.
 */
- (id) valueFromScopeForKey:(NSString *) key;

/*
 * Removes the object with the given key from scope.
 */
- (void) removeValueFromScope:(NSString *)_key;

@end

