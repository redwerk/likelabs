#import <Foundation/Foundation.h>

typedef enum {
    ContactTypePhone,
    ContactTypeEmail
} ContactType;

@interface Contact : NSObject <NSCoding>

@property (nonatomic, assign) ContactType contactType;
@property (nonatomic, retain) NSString *contactString;

- (id) initWithContactType:(ContactType)contactType andContactString:(NSString*)contactString;

@end
