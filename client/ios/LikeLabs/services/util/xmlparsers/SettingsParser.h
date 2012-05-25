#import <Foundation/Foundation.h>

@interface SettingsParser : NSObject <NSXMLParserDelegate>

@property (nonatomic, retain) NSString* companyName;
@property (nonatomic, retain) NSString* companyLogoUrl;
@property (nonatomic, retain) NSArray* promoReviews;

@end
