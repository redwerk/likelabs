#import "CustomizableSegmentedControl.h"

@interface CustomizableSegmentedControl()
@property (assign) CGFloat defaultButtonWidth;
- (void)layoutButtons;
@end

@implementation CustomizableSegmentedControl
@synthesize buttons = _buttons;
@synthesize widths = _widths;
@synthesize selectedSegmentIndex = _selectedSegmentIndex;
@synthesize dividers = _dividers;
@synthesize dividerWidth = _dividerWidth;
@synthesize defaultButtonWidth = _defaultButtonWidth;

#pragma mark - Initializatioin

- (id)initWithFrame:(CGRect)frame buttons:(NSMutableArray *)buttons widths:(NSMutableArray *)widths dividers:(NSMutableDictionary *)dividers dividerWidth:(CGFloat)dividerWidth delegate:(NSObject<CustomizableSegmentedControlDelegate> *)customizableSegmentedControlDelegate {
    if (widths && widths.count != buttons.count) {
        [NSException raise:NSInvalidArgumentException format:@"The number of widths must to be equal to the number of buttons."];
        return nil;
    }
        
    if (self = [super initWithFrame:frame]) {
        delegate = customizableSegmentedControlDelegate;
        self.buttons = buttons;
        self.widths = widths;
        self.dividers = dividers;
        _selectedSegmentIndex = 0;
        self.dividerWidth = dividerWidth;
        
        CGFloat distinctWidthsSum = 0;
        NSUInteger distinctWidthCount = 0;
        if (widths) {
            for (NSUInteger i=0; i<widths.count; i++) {
                CGFloat width = ((NSNumber*)[widths objectAtIndex:i]).floatValue;
                if (width > 0) {
                    distinctWidthsSum += width;
                    distinctWidthCount ++;
                }
            }
        }
        self.defaultButtonWidth = (frame.size.width - distinctWidthsSum - dividerWidth*(buttons.count-1))/(buttons.count - distinctWidthCount);
        
        for (UIButton* button in self.buttons) {
            [button addTarget:self action:@selector(touchDownAction:) forControlEvents:UIControlEventTouchDown];
            [button addTarget:self action:@selector(touchUpInsideAction:) forControlEvents:UIControlEventTouchUpInside];
            [button addTarget:self action:@selector(otherTouchesAction:) forControlEvents:UIControlEventTouchUpOutside];
            [button addTarget:self action:@selector(otherTouchesAction:) forControlEvents:UIControlEventTouchDragOutside];
            [button addTarget:self action:@selector(otherTouchesAction:) forControlEvents:UIControlEventTouchDragInside];
        }
        
        [self layoutButtons];        
    }
    return self;
}

#pragma mark - Memory management

- (void)dealloc
{   
    self.buttons = nil;
    self.widths = nil;
    self.dividers = nil;
    
    [_buttons release];
    [_widths release];
    [_dividers release];
    [super dealloc];
}

#pragma mark - Layout

- (void) recalculateButtonsWidths {
    CGFloat distinctWidthsSum = 0;
    NSUInteger distinctWidthCount = 0;
    if (self.widths) {
        for (NSUInteger i=0; i<self.widths.count; i++) {
            CGFloat width = ((NSNumber*)[self.widths objectAtIndex:i]).floatValue;
            if (width > 0) {
                distinctWidthsSum += width;
                distinctWidthCount ++;
            }
        }
    }
    self.defaultButtonWidth = (self.frame.size.width - distinctWidthsSum - self.dividerWidth*(self.buttons.count-1))/(self.buttons.count - distinctWidthCount);
}

- (void)layoutSubviews {
    [self layoutButtons]; 
}

- (void)layoutButtons {
    
    [self recalculateButtonsWidths];
    
    if (self.subviews.count > 0) {
        for (UIView* subview in self.subviews) {
            if ([subview respondsToSelector:@selector(removeFromSuperview)]) {
                [subview removeFromSuperview];
            }
        }
    }    
    
    CGFloat horizontalOffset = 0;
    for (NSUInteger i = 0; i<self.buttons.count; i++) {
        UIButton* button = [self.buttons objectAtIndex:i];
        
        CGFloat buttonWidth = ((NSNumber*)[self.widths objectAtIndex:i]).floatValue;
        button.frame = CGRectMake(horizontalOffset, 0.0, (buttonWidth > 0) ? buttonWidth : self.defaultButtonWidth, self.frame.size.height);        
        [self addSubview:button];
        
        if (i != self.buttons.count - 1)
        {
            NSMutableDictionary* rightStateDictionary = [self.dividers objectForKey:[NSNumber numberWithUnsignedInt:button.state]];
            UIButton* rightButton = [self.buttons objectAtIndex:i+1];
            UIImage* dividerImg = [rightStateDictionary objectForKey:[NSNumber numberWithUnsignedInt:rightButton.state]];            
            UIImageView* divider = [[[UIImageView alloc] initWithImage:dividerImg] autorelease];
            divider.frame = CGRectMake(horizontalOffset + button.frame.size.width, 0.0, self.dividerWidth, self.frame.size.height);
            [self addSubview:divider];
        }
        horizontalOffset += button.frame.size.width + self.dividerWidth;
    }
}

-(void) dimAllButtonsExcept:(UIButton*)selectedButton
{
    for (UIButton* button in self.buttons) {
        button.selected = (button == selectedButton);
    }
}

#pragma mark - Actions

- (void)setSelectedSegmentIndex:(NSInteger)selectedSegmentIndex {
    NSUInteger oldIndex = _selectedSegmentIndex;
    _selectedSegmentIndex = selectedSegmentIndex;
    
    [self dimAllButtonsExcept:[self.buttons objectAtIndex:_selectedSegmentIndex]];
    
    if([delegate respondsToSelector:@selector(selectedIndexChangedFrom:to:setnder:)]) {
        [delegate selectedIndexChangedFrom:oldIndex to:_selectedSegmentIndex setnder:self];
    }
    
    [self layoutButtons];
}

- (void)touchDownAction:(UIButton*)button
{
    self.selectedSegmentIndex = [self.buttons indexOfObject:button];
}

- (void)touchUpInsideAction:(UIButton*)button
{
    self.selectedSegmentIndex = [self.buttons indexOfObject:button];
}

- (void)otherTouchesAction:(UIButton*)button
{
    self.selectedSegmentIndex = [self.buttons indexOfObject:button];
}

@end
