//
//  KPhotoContent.m
//  Krypsis
//
//  Created by Hellboy on 30.07.09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "KPhotoContent.h"


@implementation KPhotoContent
@synthesize parameterName;
@synthesize fileName;
@synthesize contentType;
@synthesize data;
@synthesize parameters;

- (id) initWithData: (NSData *) _data 
        andFileName:(NSString *) _fileName 
   andParameterName: (NSString *) _parameterName
     andContentType: (NSString *) _contentType
andParameters: (NSDictionary *) _parameters;
{
    if (self = [super init]) {
        self.data = _data;
        self.fileName = _fileName;
        self.parameterName = _parameterName;
        self.contentType = _contentType;
        self.parameters = _parameters;
    }
    return self;
}

- (void) dealloc {
    self.fileName = nil;
    self.parameterName = nil;
    self.contentType = nil;
    [data release];
    [parameters release];
    
    [super dealloc];
}
@end
