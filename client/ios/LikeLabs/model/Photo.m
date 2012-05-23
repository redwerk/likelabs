#import "Photo.h"

@implementation Photo

@synthesize image = _image;
@synthesize status = _status;

-(id) initWithImage: (UIImage*) image {
    if (self = [super init]) {
        self.image = image;
        self.status = PhotoStatusActive;
    }
    return self;
}

- (void)dealloc {
    self.image = nil;
    [_image release];
    [super dealloc];
}

@end
