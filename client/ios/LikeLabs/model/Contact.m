#import "Contact.h"

@implementation Contact

@synthesize contactType = _contactType;
@synthesize contactString = _contactString;

- (id) initWithContactType:(ContactType)contactType andContactString:(NSString*)contactString {
    if (self = [super init]) {
        self.contactType = contactType;
        self.contactString = contactString;
    }
    return self;
}

- (void)dealloc {
    self.contactString = nil;
    [_contactString release];
    [super dealloc];
}

@end
