#import "MaskedTextFieldDelegate.h"

@interface MaskedTextFieldDelegate()

@property (nonatomic, assign) NSUInteger cursorPos;
@property (nonatomic, assign) NSObject <UITextFieldDelegate>* outerDelegate;

- (NSString*) maskPhoneNumber:(NSString*)phone inRange:(NSRange)range insertString:(NSString *)string;
- (NSUInteger)convertFromTextFieldToPhonePosition:(NSUInteger) textFieldPosition;
- (NSUInteger)convertFromPhoneToTextFieldPosition:(NSUInteger) phonePosition;
- (BOOL)isNumber:(NSString*)string;
- (NSUInteger) getStartingDigitsCountInMask;
- (NSString*) getPhoneDigitsFromString: (NSString*) string;
- (BOOL) charIsMask:(unichar)unichar;
@end

@implementation MaskedTextFieldDelegate

@synthesize mask = _mask;
@synthesize maskCharacter = _maskCharacter;
@synthesize cursorPos = _cursorPos;
@synthesize outerDelegate = _outerDelegate;

#pragma mark - Lifecycle

- (id) initWithMask:(NSString*)mask maskCharacter:(NSString*)maskCharacter andOuterDelegate: (NSObject <UITextFieldDelegate>*) outerDelegate {
    if (self = [super init]) {
        self.cursorPos = 0;
        self.outerDelegate = outerDelegate;
        self.mask = mask;
        self.maskCharacter = maskCharacter;
    }
    return self;
}

- (void)dealloc {
    self.mask = nil;
    self.maskCharacter = nil;
    [super dealloc];
}

#pragma mark - UITextFieldDelegateImplementation

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {

    textField.text = [self maskPhoneNumber:textField.text inRange:range insertString:string];
    UITextPosition* newPosition = [textField positionFromPosition:textField.beginningOfDocument offset:[self convertFromPhoneToTextFieldPosition:self.cursorPos]];
    textField.selectedTextRange = [textField textRangeFromPosition:newPosition toPosition:newPosition];
    
    return NO;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    return [self.outerDelegate textFieldShouldReturn:textField];
}

#pragma mark - Masking

- (NSString*) maskPhoneNumber:(NSString*)phone inRange:(NSRange)range insertString:(NSString *)string
{
    if (phone.length == 0) {
        phone = [NSString stringWithString:self.mask];
        range = NSMakeRange([self getFirstPlaceholerPositionInMask], 1);
    }
    
    phone = [self getPhoneDigitsFromString:phone];
    
    self.cursorPos = 0;
    NSMutableString* phoneNumber = [NSMutableString stringWithString:phone];
    self.cursorPos = [self convertFromTextFieldToPhonePosition:range.location];
    if (self.cursorPos > phone.length)
        self.cursorPos = phone.length;
    
    if (string.length == 1) { //insertion
        if ([self isNumber:string]) {
            NSUInteger i = (self.cursorPos >= phoneNumber.length) ? phoneNumber.length : self.cursorPos;
            [phoneNumber insertString:string atIndex:i];
            self.cursorPos = i;
        } else {
            self.cursorPos--;
        }
    } else { //deletion
        if (![self charIsMask:[self.mask characterAtIndex:range.location]])
            self.cursorPos--;
        if (phoneNumber.length - [self getStartingDigitsCountInMask] == 0) {
            self.cursorPos = NSNotFound;
            return self.mask;
        }
        if (range.location > [self getFirstPlaceholerPositionInMask] - 1) {
            NSUInteger i = (self.cursorPos >= phoneNumber.length) ? phoneNumber.length - 1 : self.cursorPos;
            [phoneNumber deleteCharactersInRange:NSMakeRange(i, 1)];
            self.cursorPos = (self.cursorPos >= phoneNumber.length) ? i - 1 : self.cursorPos - 1;
            self.cursorPos = (!self.cursorPos && [self getStartingDigitsCountInMask] == 1) ? NSNotFound : self.cursorPos;
        }
    }
    
    phone = phoneNumber;
    
    NSMutableString* number = [NSMutableString stringWithString:self.mask];
    for (int i = [self getStartingDigitsCountInMask]; i < phone.length; i++) {
        NSRange nextNumber = [number rangeOfString:self.maskCharacter];
        if (nextNumber.location == NSNotFound) {
            break;
        }
        [number replaceCharactersInRange:nextNumber withString:[NSString stringWithFormat:@"%c", [phone characterAtIndex:i]]];
    }
    return number;
}

- (NSUInteger)convertFromTextFieldToPhonePosition:(NSUInteger) textFieldPosition
{
    int pos = 0;
    for (int i = 0; i < textFieldPosition; i++)
    {
        if ([self charIsMask:[self.mask characterAtIndex:i]])
            pos++;
    }
    pos += [self getStartingDigitsCountInMask];
    return pos;
}

- (NSUInteger)convertFromPhoneToTextFieldPosition:(NSUInteger) phonePosition
{    
    if (phonePosition == 0) {
        return [self getFirstPlaceholerPositionInMask] + 1;
    }
    if (phonePosition == NSNotFound || phonePosition == NSMakeRange(-1, 1).location) {
        return [self getFirstPlaceholerPositionInMask];
    }
    
    int pos = 0;
    int phonePos = phonePosition - [self getStartingDigitsCountInMask]+1;
    while (phonePos && pos < self.mask.length) {
        if ([self charIsMask:[self.mask characterAtIndex:pos]])
            phonePos--;
        pos++;
    }
    return pos ? pos : [self getFirstPlaceholerPositionInMask];
}

- (BOOL)isNumber:(NSString*)string
{
    for (int i = 0; i < string.length; i++)
    {
        if (!isdigit([string characterAtIndex:i]))
            return NO;
    }
    return YES;
}

- (NSUInteger) getFirstPlaceholerPositionInMask {
    return [self.mask rangeOfString:self.maskCharacter].location;
}

- (NSUInteger) getStartingDigitsCountInMask { //in 8(___)___-____ returns 1; in +38(___)___-____ returns 2;
    int pos = 0;
    for (int i = 0; i < self.mask.length; i++)
    {
        if (isdigit([self.mask characterAtIndex:i])) {
            pos++;
        } else if ([self charIsMask:[self.mask characterAtIndex:i]]){
            break;
        }
    }
    return pos;
}

- (NSString*) getPhoneDigitsFromString: (NSString*) string {
    NSRegularExpression* regex = [NSRegularExpression regularExpressionWithPattern:@"\\D" options:NSRegularExpressionCaseInsensitive error:nil];
    return [regex stringByReplacingMatchesInString:string options:0 range:NSMakeRange(0, string.length) withTemplate:@""];
}

- (BOOL) charIsMask:(unichar)unichar {
    return [[NSString stringWithFormat:@"%c", unichar] isEqualToString:self.maskCharacter];
}

@end
