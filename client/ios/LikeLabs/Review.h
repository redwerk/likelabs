#import <Foundation/Foundation.h>

typedef enum {
    Text,
    Photo
} ReviewType;

@class User;

@interface Review : NSObject

@property (retain, nonatomic) User *user;
@property NSInteger reviewPhotoIndex;
@property(retain, nonatomic) NSArray *photos;
@property(retain, nonatomic) NSString *text;
@property ReviewType reviewType;

- (id)initWithUser:(User*)user andText:(NSString*)text;

@end
