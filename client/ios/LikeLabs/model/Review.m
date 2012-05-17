#import "Review.h"

@implementation Review
NSUInteger const MAX_CONTACTS = 5;

@synthesize user = _user;
@synthesize reviewPhotoIndex = _reviewPhotoIndex;
@synthesize reviewType = _reviewType;
@synthesize photos = _photos;
@synthesize text = _text;
@synthesize contacts = _contacts;

- (id)initWithReviewType:(ReviewType)reviewType {
    if (self = [super init]) {
        self.reviewType = reviewType;
        _photos = [[NSMutableArray alloc] init];
        _user = [[User alloc] init];
        _contacts = [[NSMutableArray alloc] initWithCapacity:MAX_CONTACTS];
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
    self.contacts = nil;
    [_photos release];
    [_text release];
    [_user release];
    [_contacts release];
    [super dealloc];
}

@end
