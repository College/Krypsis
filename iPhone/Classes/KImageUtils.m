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

#import "KImageUtils.h"
#include <math.h>

@implementation KImageUtils

static inline double radians (double degrees) 
{
    return degrees * M_PI/180;
}

static inline float min(float value1, float value2) 
{
    return value1 > value2 ? value2 :value2;
}

+ (UIImage *) scaleImage:(UIImage *)_image downToRect:(CGRect)_rect 
{
    UIImage *pointerToImage = _image;
    
    CGSize size = _image.size;
    
    float width  = size.width;
    float height = size.height;
    
    float maxWidth  = _rect.size.width;
    float maxHeight = _rect.size.height;
    
    /* If the image is smaller that the rect, do nothing */
    if (maxWidth >= width && maxHeight >= height) 
    {
        return _image;
    }
    
    /* Get the scale factor */
    float scale = min(maxWidth / width, maxHeight / height);
    _rect.size.height = scale * height;
    _rect.size.width = scale * width;
    
    CGRect scaleRect = CGRectMake(0, 0, scale * width, scale * height);
    UIGraphicsBeginImageContext(scaleRect.size);
    [pointerToImage drawInRect:scaleRect];
    _image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return _image;
}

+ (UIImage *) resizeImage:(UIImage *)_image toWidth:(NSInteger)_width andHeight:(NSInteger)_height 
{
	
	CGImageRef imageRef = [_image CGImage];
	CGImageAlphaInfo alphaInfo = CGImageGetAlphaInfo(imageRef);
	CGColorSpaceRef colorSpaceInfo = CGColorSpaceCreateDeviceRGB();
	
	if (alphaInfo == kCGImageAlphaNone) 
    {
        alphaInfo = kCGImageAlphaNoneSkipLast;
    }
	
	CGContextRef bitmap;
	
	if (_image.imageOrientation == UIImageOrientationUp || _image.imageOrientation == UIImageOrientationDown) 
    {
		bitmap = CGBitmapContextCreate(NULL, _width, _height, CGImageGetBitsPerComponent(imageRef), CGImageGetBytesPerRow(imageRef), colorSpaceInfo, alphaInfo);
		
	} 
    else 
    {
		bitmap = CGBitmapContextCreate(NULL, _height, _width, CGImageGetBitsPerComponent(imageRef), CGImageGetBytesPerRow(imageRef), colorSpaceInfo, alphaInfo);
		
	}
	
	if (_image.imageOrientation == UIImageOrientationLeft) 
    {
		NSLog(@"image orientation left");
		CGContextRotateCTM (bitmap, radians(90));
		CGContextTranslateCTM (bitmap, 0, -_height);
		
	} 
    else if (_image.imageOrientation == UIImageOrientationRight) 
    {
		NSLog(@"image orientation right");
		CGContextRotateCTM (bitmap, radians(-90));
		CGContextTranslateCTM (bitmap, -_width, 0);
		
	} 
    else if (_image.imageOrientation == UIImageOrientationUp) 
    {
		NSLog(@"image orientation up");	
		
	} 
    else if (_image.imageOrientation == UIImageOrientationDown) 
    {
		NSLog(@"image orientation down");	
		CGContextTranslateCTM (bitmap, _width, _height);
		CGContextRotateCTM (bitmap, radians(-180.));
		
	}
	
	CGContextDrawImage(bitmap, CGRectMake(0, 0, _width, _height), imageRef);
	CGImageRef ref = CGBitmapContextCreateImage(bitmap);
	UIImage *result = [UIImage imageWithCGImage:ref];
	
	CGColorSpaceRelease(colorSpaceInfo);
	CGContextRelease(bitmap);
	CGImageRelease(ref);
	
	return result;	
}

@end
