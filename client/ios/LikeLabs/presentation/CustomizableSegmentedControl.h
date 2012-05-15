@class CustomizableSegmentedControl;

@protocol CustomizableSegmentedControlDelegate
@optional
//- (void) touchUpInsideSegmentIndex:(NSUInteger)segmentIndex sender:(CustomizableSegmentedControl*)sender;
//- (void) touchDownAtSegmentIndex:(NSUInteger)segmentIndex sender:(CustomizableSegmentedControl*)sender;
- (void) selectedIndexChangedFrom:(NSUInteger)oldSegmentIndex to:(NSUInteger)newSegmentIndex setnder:(CustomizableSegmentedControl*)sender;
@end

@interface CustomizableSegmentedControl : UIView
{
    NSObject <CustomizableSegmentedControlDelegate> *delegate;
}

@property (nonatomic, copy) NSMutableArray *buttons;
@property (nonatomic, copy) NSMutableArray *widths;
@property (nonatomic, copy) NSMutableDictionary *dividers;
@property (nonatomic, assign) NSInteger selectedSegmentIndex;
@property (nonatomic, assign) CGFloat dividerWidth;

- (id) initWithFrame: (CGRect)frame buttons:(NSMutableArray*)buttons widths:(NSMutableArray*)widths dividers:(NSMutableDictionary*)dividers dividerWidth:(CGFloat)dividerWidth delegate:(NSObject <CustomizableSegmentedControlDelegate>*)customizableSegmentedControlDelegate;

@end
