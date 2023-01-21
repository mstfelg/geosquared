#import "GraphicsFactoryIos.h"
#import "ColorCache.h"
#import "BasicStrokeI.h"
#import "ColorI.h"
#import "ImageI.h"
#import "TransformI.h"
#import "IOSPrimitiveArray.h"

@implementation GraphicsFactoryIos

- (id<RXBasicStroke>)createBasicStrokeWithDouble:(jdouble)width withInt:(jint)cap withInt:(jint)join withDouble:(jdouble)miterLimit {
    return [[BasicStrokeI alloc] initWithWidth:width withMiterLimit:miterLimit withCap:cap withJoin:join];
}

-(id<RXStroke>)createBasicStrokeWithDouble:(jdouble)width withDoubleArray:(IOSDoubleArray *)dashes {
    return [[BasicStrokeI alloc] initWithWidth:width withDashes:dashes];
}

- (id <RXColor>)createColorWithInt:(jint)red
                           withInt:(jint)green
                           withInt:(jint)blue
                           withInt:(jint)alpha {
    return [[ColorCache instance] getColorWithR:red withG:green withB:blue withA:alpha];
}

- (id <RXImage>)createImageWithInt:(jint)width withInt:(jint)height withInt:(jint)type {
    return [[ImageI alloc] initWithWidth:width withHeight:height];
}

- (id <RXTransform>)createTransform {
    return [[TransformI alloc] init];
}

@end
