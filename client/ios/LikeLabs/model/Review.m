#import "Review.h"

@implementation Review

@synthesize user = _user;
@synthesize reviewPhotoIndex = _reviewPhotoIndex;
@synthesize reviewType = _reviewType;
@synthesize photos = _photos;
@synthesize text = _text;

- (id)initWithReviewType:(ReviewType)reviewType {
    if (self = [super init]) {
        self.reviewType = reviewType;
        _photos = [[NSMutableArray alloc] init];
    }
    return self;
}

- (id)initWithUser:(User*)user andText:(NSString*)text {
    if (self = [self initWithReviewType: ReviewTypeText]) {
        self.user = user;
        self.text = text;
    }
    return self;
}

- (void)dealloc {
    self.photos = nil;
    self.text = nil;
    self.user = nil;
    [_photos release];
    [_text release];
    [_user release];
    [super dealloc];
}

@end
