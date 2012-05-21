#import <Foundation/Foundation.h>

@interface User : NSObject

@property(nonatomic, retain) NSString *name;
@property(nonatomic, retain) NSString *phone;

- (id) initWtithName:(NSString *) name;
- (id) initWithPhone:(NSString *) phone;

@end
