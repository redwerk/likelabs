#import "Photo.h"

@implementation Photo

static NSString* const IMAGE_KEY = @"image";
static NSString* const STATUS_KEY = @"status";

@synthesize image = _image;
@synthesize status = _status;

-(id) initWithImage: (UIImage*) image {
    if (self = [super init]) {
        self.image = image;
        self.status = PhotoStatusActive;
    }
    return self;
}

- (void)encodeWithCoder:(NSCoder *)aCoder {
    [aCoder encodeObject:self.image forKey:IMAGE_KEY];
    [aCoder encodeObject:[NSNumber numberWithUnsignedInt:self.status] forKey:STATUS_KEY];
}

- (id)initWithCoder:(NSCoder *)aDecoder {
    if (self = [super init])  {
        self.image = [aDecoder decodeObjectForKey:IMAGE_KEY];
        self.status = ((NSNumber*)[aDecoder decodeObjectForKey:STATUS_KEY]).unsignedIntegerValue;        
    }
    return self;
}

- (void)dealloc {
    self.image = nil;
    [_image release];
    [super dealloc];
}

@end
