#import <Foundation/Foundation.h>

@interface MaskedTextFieldDelegate : NSObject <UITextFieldDelegate>

@property (nonatomic, retain) NSString* mask;
@property (nonatomic, retain) NSString* maskCharacter;

- (id) initWithMask:(NSString*)mask maskCharacter:(NSString*)maskCharacter andOuterDelegate: (NSObject <UITextFieldDelegate>*) outerDelegate;
- (NSUInteger) getFirstPlaceholerPositionInMask;

@end
