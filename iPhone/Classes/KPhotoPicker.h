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
#import "KPhotoPickerProtocol.h"

@interface KPhotoPicker :NSObject <UIImagePickerControllerDelegate, UINavigationControllerDelegate>  {
    id protocol;
}

@property (nonatomic, assign) id protocol;

- (id) initWithPickerProtocol:(NSObject<KPhotoPickerProtocol> *) _protocol;

/**
 * Starts picking image action and create the image picker 
 * controller on top of the given parent controller.
 */
- (void) pickWithParentController:(UIViewController *) parent;

/**
 * Image was picket or shot by user.
 * Process the image and destroy this control.
 */
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info;

/**
 * The user canceled the operation.
 * Destroy this control and notify the view.
 */
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker;

@end
