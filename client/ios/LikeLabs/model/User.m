#import "User.h"

@implementation User

static NSString* const NAME_KEY = @"name";
static NSString* const PHONE_KEY = @"phone";

@synthesize name = _name;
@synthesize phone = _phone;

#pragma mark - Initialization

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

#pragma mark - Serialization

- (void)encodeWithCoder:(NSCoder *)aCoder {
    [aCoder encodeObject:self.name forKey:NAME_KEY];
    [aCoder encodeObject:self.phone forKey:PHONE_KEY];
}

- (id)initWithCoder:(NSCoder *)aDecoder {
    if (self = [super init]) {
        self.name = [aDecoder decodeObjectForKey:NAME_KEY];
        self.phone = [aDecoder decodeObjectForKey:PHONE_KEY];
    }
    return self;
}

#pragma mark - Memory management

- (void)dealloc {
    self.name = nil;
    self.phone = nil;
    [_name release];
    [_phone release];
    [super dealloc];
}

@end
