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

#import "KResponse.h"

@implementation KResponse

@synthesize request;
@synthesize parameters;

- (id) initWithRequest:(KRequest *)_request
{
	if (self = [super init])
	{
		self.request = _request;
		self.parameters = [[NSMutableDictionary alloc] init];
	}
	return self;
}


- (NSString*) javaScriptValue
{
	NSMutableString *function = [NSMutableString stringWithString:@"Krypsis.Device."];
    
    [function appendString:[self capitalize:self.request.moduleName]];
    [function appendString:@"."];
    [function appendString:[self javaScriptResultType]];
    [function appendString:@"."];
	[function appendString:request.actionName];
	[function appendString:[NSString stringWithFormat:@"(%@)", [self parametersStringValue]]];
    
	NSLog(@"JavaScript Function:%@", function);
	
	return function;
}

- (NSString*) javaScriptResultType
{
    [self doesNotRecognizeSelector:_cmd];
    return nil;
}

- (NSString *) capitalize:(NSString *)_string
{
	return [_string stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:[[_string substringToIndex:1] uppercaseString]];
}


- (NSString *)parametersStringValue
{
	if (!self.parameters || [self.parameters count] == 0)
	{
		return @"{}";
	}
	
	NSMutableString *args = [NSMutableString stringWithString:@""];
	
	for (NSString *key in self.parameters) 
	{
		NSString* value = [self.parameters valueForKey:key];
		if (args && [args length] > 0) 
		{
			[args appendString:@", "];
		} 
		
        [args appendString:[NSString stringWithFormat:@"%@:", key]];
        
        if ([value isKindOfClass:[NSString class]])
        {
            [args appendString:[NSString stringWithFormat:@"'%@'", value]];
        }
        else 
        {
            [args appendString:[NSString stringWithFormat:@"%@", value]];
        }
		
	}
	
	return [NSString stringWithFormat:@"{%@}", args];
}

- (void) dealloc 
{
    [request release];
    [parameters release];
    
    [super dealloc];
}

@end
