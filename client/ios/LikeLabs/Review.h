#import <Foundation/Foundation.h>

typedef enum {
    Text,
    Photo
} ReviewType;

@interface Review : NSObject

@property int reviewPhotoIndex;
@property(strong) NSArray *photos;
@property(strong) NSString *text;
@property ReviewType reviewType;

@end
