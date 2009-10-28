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

#import "KrypsisWebViewController.h"


@implementation KrypsisWebViewController
@synthesize webView;
@synthesize dispatcher;
@synthesize loaded;

- (void) viewDidLoad {
    [super viewDidLoad];
    
    if (!self.loaded) {
        [self navigateTo:@"http://demo.krypsis.org/demo/index.html"];
        //[self navigateTo:@"http://localhost:9494/demo/index.html"];
        self.loaded = true;
    }
}

- (void) navigateTo:(NSString *) urlFromString {
    NSURL *url = [NSURL URLWithString:urlFromString];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    [webView loadRequest:request];
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];

	@try {
		KRequest *kRequest = [[[KRequest alloc] init] autorelease];
        [kRequest parseUrl:[request URL]];
        
        if (self.dispatcher) {
            NSLog(@"Dispatching request %@.%@", [kRequest moduleName], [kRequest actionName]);
            [self.dispatcher dispatchRequest:kRequest];
        }
        else {
            @throw [NSException exceptionWithName:@"Request Exception" reason:@"Dispatcher is not set." userInfo:nil];
        }
        
        return NO;
	}
	@catch (NSException * e) {
        NSLog(@"This request is not targeting krypsis:%@", [e description]);
		return YES;
	}
    @finally {
        [pool release];
    }
    
    return YES;
}

- (void) setRequestDispatcher:(NSObject<KRequestDispatchProtocol> *) _dispatcher {
    self.dispatcher = _dispatcher;
}

- (void) dispatchJavaScriptFunctionWithName:(NSString *) javaScriptFunction {
    if (javaScriptFunction && [javaScriptFunction length] > 0) {
        [webView stringByEvaluatingJavaScriptFromString:javaScriptFunction];
    }
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    return YES;
}


- (void) dealloc {
    [dispatcher release];
    [webView release];
    [super dealloc];
}


@end
