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

#import <Foundation/Foundation.h>
#import "KHttpPostMessageContent.h"
#import "KHttpPostMessage.h"


@interface KPhotoUploader :NSObject {
    id delegate;
    KHttpPostMessage *httpMessage;
}

@property (nonatomic, assign) id                delegate;
@property (nonatomic, retain) KHttpPostMessage  *httpMessage;

/**
 * Creates a new uploader for the given url and content
 */
- (id) initWithMessage:(KHttpPostMessage *)_message;

/**
 * Uploads the contant and returns the server
 * response as data
 */
- (NSURLConnection *) upload:(id)_delegate;
@end
