//
//  KPhotoContent.h
//  Krypsis
//
//  Created by Hellboy on 30.07.09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface KPhotoContent : NSObject {
    NSString *fileName;
    NSString *parameterName;
    NSString *contentType;
    NSData   *data;
    NSDictionary *parameters;
}

@property (nonatomic, copy)   NSString *fileName;
@property (nonatomic, copy)   NSString *parameterName;
@property (nonatomic, copy)   NSString *contentType;
@property (nonatomic, retain) NSData   *data;
@property (nonatomic, retain) NSDictionary *parameters;

- (id) initWithData: (NSData *) _data 
        andFileName: (NSString *) _fileName 
   andParameterName: (NSString *) _parameterName
     andContentType: (NSString *) _contentType
      andParameters: (NSDictionary *) _parameters;

@end
