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

@interface KrypsisWebViewController :UIViewController {
    UIWebView *webView;
    NSObject<KRequestDispatchProtocol> *dispatcher;
    BOOL loaded;
}

@property BOOL loaded;
@property (nonatomic, retain) IBOutlet UIWebView *webView;
@property (nonatomic, retain) NSObject<KRequestDispatchProtocol> *dispatcher;

/**
 * Sets the request dispatcher for this controller.
 * The dispatcher implements the request dispatcher protocol
 * and is responsible for the given requests.
 */
- (void) setRequestDispatcher:(NSObject<KRequestDispatchProtocol> *) _dispatcher;

/**
 * Takes the given JavaScript function
 * and deligates it to the browser. The browser
 * will interpret this string as a JavaScript call and
 * execute the function.
 */
- (void) dispatchJavaScriptFunctionWithName:(NSString *) javaScriptFunction;

/**
 * Loads the given url into the webview component.
 */
- (void) navigateTo:(NSString *) urlFromString;
@end
