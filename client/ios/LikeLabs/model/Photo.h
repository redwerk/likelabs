#import <Foundation/Foundation.h>

typedef enum {
    PhotoStatusSelected,
    PhotoStatusDeleted,
    PhotoStatusActive
} PhotoStatus;

@interface Photo : NSObject <NSCoding>

@property (nonatomic, retain) UIImage* image;
@property (nonatomic, assign) PhotoStatus status;

-(id) initWithImage: (UIImage*) image;

@end
