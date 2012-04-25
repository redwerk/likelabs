#import "Review.h"

@implementation Review

@synthesize user = _user;
@synthesize reviewPhotoIndex = _reviewPhotoIndex;
@synthesize reviewType = _reviewType;
@synthesize photos = _photos;
@synthesize text = _text;

- (id)initWithUser:(User*)user andText:(NSString*)text {
    if (self = [super init]) {
        self.user = user;
        self.text = text;
    }
    return self;
}

- (void)dealloc {
    self.photos = nil;
    self.text = nil;
    self.user = nil;
    [super dealloc];
}

@end
