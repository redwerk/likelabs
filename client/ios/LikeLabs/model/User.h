#import <Foundation/Foundation.h>

@interface User : NSObject

@property(nonatomic, retain) NSString *name;
@property(nonatomic, retain) NSString *phoneNumber;
@property(nonatomic, retain) NSString *password;

- (id) initWtithName:(NSString *) name phone:(NSString *)phone andPassword:(NSString *)password;

@end
