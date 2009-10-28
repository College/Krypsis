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

#import "KHttpPostMessageContent.h"


@implementation KHttpPostMessageContent

@synthesize headers;
@synthesize data;

- (id) initWithString:(NSString *)_string 
{
    return [self initWithData:[_string dataUsingEncoding:NSISOLatin1StringEncoding]];
}

- (id) initWithData:(NSData *)_data 
{
    if (self = [super init]) 
    {
        self.data = _data;
        self.headers = [[NSMutableDictionary alloc] init];
    }
    return self;
}

- (void) setContentType:(NSString *)_type 
{
    [self addHeader:_type forKey:@"Content-Type"];
}

- (void) setContentDisposition:(NSString *)_disposition 
{
    [self addHeader:_disposition forKey:@"Content-Disposition"];
}

- (void) setContentLength:(int)_length 
{
    [self addHeader:[NSNumber numberWithInt:_length] forKey:@"Content-Length"];
}

- (void) addHeader:(id)_value forKey:(NSString *)_key 
{
    [self.headers setValue:_value forKey:_key];
}

- (NSString *) contentAsString
{
    NSMutableString *buffer = [[[NSMutableString alloc] init] autorelease];
    
    for (NSString *key in headers) 
    {
        NSString *string = [NSString stringWithFormat:@"%@: %@\r\n", key, [headers valueForKey:key]];
        [buffer appendString:string];
    }
    
    if ([buffer length] > 0) 
    {
        [buffer appendString:@"\r\n"];
    }
    
    NSLog(@"created content for uploading: %@", buffer);
    return buffer;
}

- (NSData *) contentAsData 
{
    NSMutableData *buffer = [[[NSMutableData alloc] init] autorelease];
    [buffer appendData:[[self contentAsString] dataUsingEncoding:NSISOLatin1StringEncoding]];
    
    if ([data length] > 0) 
    {
        [buffer appendData:data];
    }
    
    return buffer;
}

- (void) dealloc 
{
    [data release];
    [headers release];
    
    [super dealloc];
}

@end
