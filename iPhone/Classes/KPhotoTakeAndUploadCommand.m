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

#import "KPhotoTakeAndUploadCommand.h"
#import "KErrorResponse.h";
#import "KErrors.h"
#import "KPhotoPickerAndUploader.h"
#import "KDateUtils.h"

@implementation KPhotoTakeAndUploadCommand

- (void) executeWithDelegate:(KrypsisAppDelegate *) appDelegate andRequest:(KRequest *) request {
    
    /*
     Extract the required parameters and set default
     values if neccessary.
     */
    NSString *uploadUrl                 = [request.parameters valueForKey:@"uploadurl"];
    NSString *fileName                  = [self filenameFromDictionary:request.parameters];
    NSString *parameterName             = [self parameterNameFromDictionary:request.parameters];
    NSDictionary *remainingParameters   = [self copyRemainingParameters:request.parameters];
    
    /* Check if all needed parameters are available */
    if (!uploadUrl) {
        @throw [NSException exceptionWithName:@"Request Exception" 
                                       reason:@"Paramater 'uploadurl' is missing" 
                                     userInfo:[NSDictionary dictionaryWithObject:[NSNumber numberWithInt:ERROR_INVALID_PARAMETER] 
                                                                           forKey:@"code"]];
    }
    NSLog(@"Image will be uploaded to: %@", uploadUrl);
 
    /* This instance will destroy itself after finishing work */
    KPhotoPickerAndUploader *pickerAndUploader = [[KPhotoPickerAndUploader alloc] initWithAppDelegate:appDelegate request:request];
    
    KHttpPostMessage *message = [[KHttpPostMessage alloc] initWithUrl:[NSURL URLWithString:uploadUrl]];
    if (remainingParameters && [remainingParameters count] > 0) {
        for (NSString *key in remainingParameters) {
            NSLog(@"Set param %@ to value %@", key, [remainingParameters valueForKey:key]);
            [message addValue:[remainingParameters valueForKey:key] forName:key];
        }
    }
    
    [pickerAndUploader setHttpMessage:message];
    [pickerAndUploader setParameterName:parameterName];
    [pickerAndUploader setFileName:fileName];
    
    /* Start doing the work... */
    [pickerAndUploader pickAndUpload];
    
    /* Release the created instances */
    [remainingParameters release];
    [message release];
}

- (NSDictionary *) copyRemainingParameters:(NSDictionary *) fromParams {
    if (!fromParams || [fromParams count] <= 0) {
        return nil;
    }
    
    NSDictionary *result = [[NSMutableDictionary alloc] init];
    NSArray *reserved = [NSArray arrayWithObjects:@"parametername", @"filename", @"uploadurl", nil];
    
    for (NSString *key in fromParams) {
        if (![reserved containsObject:key]) {
            [result setValue:[fromParams valueForKey:key] forKey:key];
        }
    }
    
    return result;
}

- (NSString *) parameterNameFromDictionary:(NSDictionary *) params {
    NSString *result = [params valueForKey:@"parametername"];
    if (!result) {
        result = @"file";
    }
    return result;
}

- (NSString *) filenameFromDictionary:(NSDictionary *) params {
    NSString *result = [params valueForKey:@"filename"];
    if (!result) {
        result = [NSString stringWithFormat:@"image%@.jpg", [KDateUtils milliSecondsFromDate:[NSDate date]]];
    }
    return result;
}

@end
