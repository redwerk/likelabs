#import "MaskedTextField.h"

@interface MaskedTextField()
@property (nonatomic, assign) NSUInteger cursorPos;
- (NSString*) maskPhoneNumber:(NSString*)phone inRange:(NSRange)range insertString:(NSString *)string;
- (NSRange)convertFromTextFieldToPhoneRange:(NSRange) range;
- (NSRange)convertFromPhoneToTextFieldRange:(NSRange) range;
- (BOOL)isNumber:(NSString*)string;
- (NSUInteger) getFirstPlaceholerPositionInMask;
- (NSString*) getPhoneDigitsFromString: (NSString*) string;
- (BOOL) charIsMask:(unichar)unichar;
@end

@implementation MaskedTextField
@synthesize mask = _mask;
@synthesize maskCharacter = _maskCharacter;
@synthesize cursorPos = _cursorPos;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.delegate = self;
        self.cursorPos = 0;
    }
    return self;
}

- (void)dealloc {
    self.mask = nil;
    self.maskCharacter = nil;
    [super dealloc];
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    
    self.text = [self maskPhoneNumber:self.text inRange:range insertString:string];
    
    NSRange currentCursorPos = [self convertFromPhoneToTextFieldRange:NSMakeRange(self.cursorPos, 1)];
    UITextPosition* newPosition = [textField positionFromPosition:textField.beginningOfDocument offset:currentCursorPos.location];
    textField.selectedTextRange = [textField textRangeFromPosition:newPosition toPosition:newPosition];
    
    return NO;
}

- (NSString*) maskPhoneNumber:(NSString*)phone inRange:(NSRange)range insertString:(NSString *)string
{
    if (phone.length == 0)
    {
        phone = [NSString stringWithString:self.mask];
        range = NSMakeRange([self getFirstPlaceholerPositionInMask], 1);
    }
    
    phone = [self getPhoneDigitsFromString:phone];
    
    NSMutableString* phoneNumber = [NSMutableString stringWithString:phone];
    self.cursorPos = [self convertFromTextFieldToPhoneRange:range].location;
    if (self.cursorPos > phone.length)
        self.cursorPos = phone.length;
    
    if (string.length == 1)
    {
        if ([self isNumber:string]) {
            NSUInteger i = (self.cursorPos >= phoneNumber.length) ? phoneNumber.length : self.cursorPos;
            [phoneNumber insertString:string atIndex:i];
            self.cursorPos = i;
        } else {
            self.cursorPos--;
        }
    }
    else
    {
        if (![self charIsMask:[self.mask characterAtIndex:range.location]])
            self.cursorPos--;
        if (range.location > [self getFirstPlaceholerPositionInMask] - 1) {
            NSUInteger i = (self.cursorPos >= phone.length) ? phone.length - 1 : self.cursorPos;
            [phoneNumber deleteCharactersInRange:NSMakeRange(i, 1)];
            self.cursorPos = (self.cursorPos >= phone.length) ? i - 1 : self.cursorPos - 1;
        }
    }
    
    phone = phoneNumber;
    
    NSMutableString* number = [NSMutableString stringWithString:self.mask];
    
    for (int i = [self getStartingDigitsCountInMask]; i < phone.length; i++)
    {
        NSRange nextNumber = [number rangeOfString:self.maskCharacter];
        if (nextNumber.location == NSNotFound)
        {
            break;
        }
        
        [number replaceCharactersInRange:nextNumber withString:[NSString stringWithFormat:@"%c", [phone characterAtIndex:i]]];
    }
    
    return number;
}

- (NSRange)convertFromTextFieldToPhoneRange:(NSRange) range
{
    int pos = 0;
    for (int i = 0; i < range.location; i++)
    {
        if ([self charIsMask:[self.mask characterAtIndex:i]])
            pos++;
    }
    pos += [self getStartingDigitsCountInMask];
    return NSMakeRange(pos, 1);
}

- (NSRange)convertFromPhoneToTextFieldRange:(NSRange) range
{
    if (range.location == 0 || range.location == NSNotFound || range.location == NSMakeRange(-1, 1).location)
        return NSMakeRange([self getFirstPlaceholerPositionInMask],1);
    
    int pos = 0;
    int phonePos = range.location - [self getStartingDigitsCountInMask]+1;
    while (phonePos && pos < self.mask.length) {
        if ([self charIsMask:[self.mask characterAtIndex:pos]])
            phonePos--;
        pos++;
    }
    return NSMakeRange(pos ? pos : [self getFirstPlaceholerPositionInMask], 1);
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
