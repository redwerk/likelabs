#import "Contact.h"

@implementation Contact

static NSString* const CONTACT_TYPE_KEY = @"contactType";
static NSString* const CONTACT_STRING_KEY = @"contactString";

@synthesize contactType = _contactType;
@synthesize contactString = _contactString;

- (id) initWithContactType:(ContactType)contactType andContactString:(NSString*)contactString {
    if (self = [super init]) {
        self.contactType = contactType;
        self.contactString = contactString;
    }
    return self;
}

- (void)encodeWithCoder:(NSCoder *)aCoder {
    [aCoder encodeObject:[NSNumber numberWithUnsignedInt:self.contactType] forKey:CONTACT_TYPE_KEY];
    [aCoder encodeObject:self.contactString forKey:CONTACT_STRING_KEY];
}

- (id)initWithCoder:(NSCoder *)aDecoder {
    if (self = [super init]) {
        self.contactType = ((NSNumber*)[aDecoder decodeObjectForKey:CONTACT_TYPE_KEY]).unsignedIntegerValue;
        self.contactString = [aDecoder decodeObjectForKey:CONTACT_STRING_KEY];
    }
    return self;
}

- (void)dealloc {
    self.contactString = nil;
    [_contactString release];
    [super dealloc];
}

@end
