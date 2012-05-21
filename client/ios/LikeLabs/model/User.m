#import "User.h"

@implementation User

@synthesize name = _name;
@synthesize phone = _phone;

- (id) initWtithName:(NSString *) name {
    if (self = [super init]) {
        self.name = name;
    }
    return self;
}

- (id)initWithPhone:(NSString *)phone {
    if (self = [super init]) {
        self.phone = phone;
    }
    return self;
}

- (void)dealloc {
    self.name = nil;
    self.phone = nil;
    [self.name release];
    [self.phone release];
    [super dealloc];
}

@end
