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

#import "KrypsisAppDelegate.h"

#import "KBaseGetMetaDataCommand.h"
#import "KBaseResetCommand.h"

#import "KScreenGetResolutionCommand.h"
#import "KScreenStartOrientationListenerCommand.h"
#import "KScreenStopOrientationListenerCommand.h"

#import "KLocationGetLocationCommand.h"
#import "KLocationStartObserverLocationCommand.h"
#import "KLocationStopObserveLocationCommand.h"

#import "KPhotoTakeAndUploadCommand.h"

#import "KIdentity.h"
#import "KErrors.h"
#import "KErrorResponse.h"

@implementation KrypsisAppDelegate

@synthesize window;
@synthesize webViewController;
@synthesize navigationController;
@synthesize modules;
@synthesize scope;


- (void)applicationDidFinishLaunching:(UIApplication *)application {    
    self.modules = [[NSMutableDictionary alloc] init];
    self.scope   = [[NSMutableDictionary alloc] init];

    [webViewController setRequestDispatcher:self];
    [self introduceCommands];
    
    [window addSubview:[navigationController view]];
    [navigationController pushViewController:webViewController animated:YES];
    // Override point for customization after application launch
    [window makeKeyAndVisible];
}

- (void) dispatchRequest:(KRequest *) request {
    NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
    
    @try {
        
        /*
         Get the module registry and the module that is targeted by 
         module name in the request
         */
        Class commandClass = [self commandClassForModule:request.moduleName andAction:request.actionName];
        
        /* Stop the process if the module is not found */
        if (!commandClass) {
            @throw [NSException exceptionWithName:@"Request Dispatch Exception" 
                                           reason:@"Responsible command class not found" 
                                         userInfo:[NSDictionary dictionaryWithObject:[NSNumber numberWithInt:ERROR_COMMAND_NOT_SUPPORTED] forKey:@"code"]];
        }
        
        /*
         Create a new instance of the command and execute it
         */
        id<KCommandProtocol> command = [[[commandClass alloc] init] autorelease];
        [command executeWithDelegate:self andRequest:request];
    }
    @catch (NSException *e) {
        NSNumber *code = [e userInfo] && [[e userInfo] count] > 0 ? [[e userInfo] valueForKey:@"code"] :[NSNumber numberWithInt:ERROR_UNKNOWN];
        KResponse *response = [[[KErrorResponse alloc] initWithRequest:request andWithCode:[code intValue]] autorelease];
        [self dispatchResponse:response];
    }
    @finally {
        [pool release];
    }
}

- (void) dispatchResponse:(KResponse *) response {
    @try {
        [webViewController dispatchJavaScriptFunctionWithName:[response javaScriptValue]];
    }
    @catch (NSException *e) {
        NSLog(@"Exception in dispatching response:%@", [e description]);
    }
}

- (NSString *) krypsisId {
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    NSString *id = [prefs stringForKey:@"krypsis.kid"];
    
    if (!id) {
        id = [KIdentity generate];
        NSLog(@"Generated Krypsis ID:%@", id);
        [prefs setObject:id forKey:@"krypsis.kid"];
        [prefs synchronize];
    }
    else  {
        NSLog(@"Loaded krypsis ID from settings:%@", id);
    }
    return id;
}

- (void) registerCommandClass:(id) clazz forModule:(NSString *) moduleName andAction:(NSString *) actionName {
    /* If no dictionary exists for the given module name, then create */
    NSMutableDictionary *module = [self.modules valueForKey:moduleName];
    if (!module) {
        module = [[[NSMutableDictionary alloc] init] autorelease];
        [self.modules setValue:module forKey:moduleName];
    }
    
    NSLog(@"Register new command class %@ with module name %@ and action %@", clazz, moduleName, actionName);
    
    /* Assign the give class with the action name within the module */
    [module setValue:clazz forKey:actionName];
}

- (id) commandClassForModule:(NSString *) moduleName andAction:(NSString *) actionName {
    if (!self.modules) { return nil; }
    
    /* Get the dictionary that is associated with the module name */
    NSMutableDictionary *module = [self.modules valueForKey:moduleName];
    if (!module) {
        return nil; 
    }
    
    return [module valueForKey:actionName];
}

- (void) putValueInScope:(NSObject *) value forKey:(NSString *) key {
    [self.scope setValue:value forKey:key];
}

- (void) removeValueFromScope:(NSString *)_key {
    [self.scope removeObjectForKey:_key];
}

- (id) valueFromScopeForKey:(NSString *) key {
    return [self.scope valueForKey:key];
}

- (void) introduceCommands {
    
    /* BASE commands */
    [self registerCommandClass:[KBaseGetMetaDataCommand class]                 
                     forModule:@"base"
                     andAction:@"getmetadata"];
    
    [self registerCommandClass:[KBaseResetCommand class]                 
                     forModule:@"base"
                     andAction:@"reset"];    
    
    /* SCREEN commands */
    [self registerCommandClass:[KScreenGetResolutionCommand class]
                     forModule:@"screen"
                     andAction:@"getresolution"]; 
    
    [self registerCommandClass:[KScreenStartOrientationListenerCommand class]
                     forModule:@"screen"
                     andAction:@"startobserveorientation"]; 
    
    [self registerCommandClass:[KScreenStopOrientationListenerCommand class]
                     forModule:@"screen"
                     andAction:@"stopobserveorientation"]; 

    /* LOCATION commands */
    [self registerCommandClass:[KLocationGetLocationCommand class]
                     forModule:@"location"
                     andAction:@"getlocation"]; 

    [self registerCommandClass:[KLocationStartObserverLocationCommand class]
                     forModule:@"location"
                     andAction:@"startobservelocation"]; 

    [self registerCommandClass:[KLocationStopObserveLocationCommand class]
                     forModule:@"location"
                     andAction:@"stopobservelocation"]; 
    
    /* PHOTO commands */
    [self registerCommandClass:[KPhotoTakeAndUploadCommand class]
                     forModule:@"photo"
                     andAction:@"takeandupload"];     
}

- (void) dealloc {
    [scope release];
    [modules release];
    [webViewController release];
    [navigationController release];
    [window release];
    [super dealloc];
}


@end
