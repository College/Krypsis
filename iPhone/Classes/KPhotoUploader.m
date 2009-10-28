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

#import "KPhotoUploader.h"
#import "KStringUtils.h"

@implementation KPhotoUploader

@synthesize delegate;
@synthesize httpMessage;

- (id) initWithMessage:(KHttpPostMessage *)_message {
    if (self = [super init]) {
        self.httpMessage = _message;
    }
    
    return self;
}

- (NSURLConnection *) upload:(id)_delegate {
    
    NSLog(@"Upload data to: %@", [httpMessage url]);
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[httpMessage url]];
    [request setHTTPMethod:@"POST"];
    
    for (NSString *key in [self.httpMessage props]) {
        NSLog(@"Add property: %@ = %@", key, [[self.httpMessage props] valueForKey:key]);
        [request setValue:[[self.httpMessage props] valueForKey:key] forHTTPHeaderField:key];
    }
    
    
    [request setHTTPBody:[self.httpMessage contentAsData]];
    return [NSURLConnection connectionWithRequest:request delegate:_delegate];
}

- (void) dealloc {
    [httpMessage release];
    self.delegate = nil;
    
    [super dealloc];
}
@end
