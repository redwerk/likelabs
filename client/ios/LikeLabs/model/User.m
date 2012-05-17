#import "User.h"

@implementation User

@synthesize name = _name;
@synthesize phoneNumber = _phoneNumber;
@synthesize password = _password;

- (id) init {
    if (self = [super init]) {
        _name = [[NSString alloc] init];
        _phoneNumber = [[NSString alloc] init];
        _password = [[NSString alloc] init];
    }
    return self;
}

- (id) initWtithName:(NSString *) name phone:(NSString *)phone andPassword:(NSString *)password {
    if (self = [super init]) {
        self.name = name;
        self.phoneNumber = phone;
        self.password = password;
    }
    return self;
}

- (void)dealloc {
    self.name = nil;
    self.phoneNumber = nil;
    self.password = nil;
    [super dealloc];
}

@end
