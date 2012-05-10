#import <Foundation/Foundation.h>

typedef enum {
    ReviewTypeText,
    ReviewTypePhoto
} ReviewType;

@class User;

@interface Review : NSObject

@property (retain, nonatomic) User *user;
@property (assign) NSInteger reviewPhotoIndex;
@property (retain, nonatomic) NSMutableArray *photos;
@property (retain, nonatomic) NSString *text;
@property (assign) ReviewType reviewType;

- (id)initWithReviewType: (ReviewType)reviewType;
- (id)initWithUser: (User *)user andText: (NSString *)text;

@end
