#import <Foundation/Foundation.h>
#import "User.h"
#import "Contact.h"
#import "Photo.h"

typedef enum {
    ReviewTypeText,
    ReviewTypePhoto
} ReviewType;

@interface Review : NSObject
extern NSUInteger const MAX_CONTACTS;

@property (retain, nonatomic) User *user;
@property (assign, nonatomic) NSInteger reviewPhotoIndex;
@property (retain, nonatomic) NSMutableArray *photos;
@property (retain, nonatomic) NSString *text;
@property (assign, nonatomic) ReviewType reviewType;
@property (retain, nonatomic) NSMutableArray* contacts;

- (id)initWithReviewType: (ReviewType)reviewType;
- (id)initWithUser: (User *)user andText: (NSString *)text;
- (NSString*) serializeToXml;

@end
