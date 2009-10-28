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

#import "KRequest.h"
#import "KGlobals.h"
#import "RegexKitLite.h"

@implementation KRequest

@synthesize moduleName;
@synthesize actionName;
@synthesize parameters;

- (void) parseUrl:(NSURL *)_url 
{
    
    if (![[_url absoluteString] hasPrefix:[KGlobals KRYPSIS_NAMESPACE]]) 
    {
        @throw [NSException exceptionWithName:@"Namespace exception" reason:[NSString stringWithFormat:@"%@ => not a Krypsis URL", _url] userInfo:nil];
    }
		
    NSString *moduleAndAction = [self removeNamespaceFromPath:[_url path]];
    NSArray *parts = [moduleAndAction componentsMatchedByRegex:@"\\w+"];
    
    if (!parts || [parts count] != 2) 
    {
        @throw [NSException exceptionWithName:[NSString stringWithFormat:@"Could not extract module and action name from:%@", moduleAndAction] reason:nil userInfo:nil];
    }
        
	self.moduleName = [parts objectAtIndex:0];
    self.actionName = [parts objectAtIndex:1];
    self.parameters = [self parametersFromString:[_url query]];
}

- (NSString *) removeNamespaceFromPath:(NSString *)_path 
{
    NSRange range = [_path rangeOfString:[KGlobals KRYPSIS_PATH]];
    return [_path stringByReplacingCharactersInRange:range withString:@""];
}

- (NSDictionary *) parametersFromString:(NSString*)_parameterString 
{
    NSCharacterSet* delimiterSet = [NSCharacterSet characterSetWithCharactersInString:@"&;"] ;
    NSMutableDictionary* pairs = [NSMutableDictionary dictionary] ;

    if (!_parameterString) 
    {
        return pairs;
    }
    
    NSScanner* scanner = [[NSScanner alloc] initWithString:_parameterString] ;
    
    while (![scanner isAtEnd]) 
    {
        NSString* pairString ;
        [scanner scanUpToCharactersFromSet:delimiterSet intoString:&pairString] ;
        [scanner scanCharactersFromSet:delimiterSet intoString:NULL] ;
        NSArray* pair = [pairString componentsSeparatedByString:@"="] ;
        
        if ([pair count] == 2) 
        {
            NSString* key = [[pair objectAtIndex:0] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding] ;
            NSString* value = [[pair objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding] ;
            [pairs setObject:value forKey:key] ;
        }
    }
    
    [scanner release];
    return [NSDictionary dictionaryWithDictionary:pairs] ;
}

- (void) dealloc 
{
	[parameters release];
    self.actionName = nil;
    self.moduleName = nil;
    
    [super dealloc];
}

@end
