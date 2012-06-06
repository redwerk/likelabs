#import <UIKit/UIKit.h>

@interface MaskedTextField : UITextField <UITextFieldDelegate>

@property (nonatomic, retain) NSString* mask;
@property (nonatomic, retain) NSString* maskCharacter;
- (NSUInteger) getStartingDigitsCountInMask;
@end
