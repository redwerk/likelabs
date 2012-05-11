#import <Foundation/Foundation.h>

typedef enum {
    Phone,
    Email
} ContactType;

@interface Contact : NSObject

@property ContactType contactType;
@property(strong) NSString *contactString;

@end
