#import <UIKit/UIKit.h>
#import "ChildController.h"

@interface TextReviewController : UIViewController <UITableViewDataSource, UITableViewDelegate, ChildController>

@property (retain, nonatomic) IBOutlet UITableView *socialComments;
@property (retain, nonatomic) IBOutlet UITextView *textView;
@property (retain, nonatomic) IBOutlet UIButton *btnNext;
@property (retain, nonatomic) IBOutlet UILabel *textLabel;

- (IBAction)goHome:(id)sender;

@end
