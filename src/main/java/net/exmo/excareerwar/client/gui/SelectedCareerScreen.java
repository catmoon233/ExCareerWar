package net.exmo.excareerwar.client.gui;


import com.mojang.authlib.GameProfile;
import net.exmo.excareerwar.Excareerwar;
import net.exmo.excareerwar.content.Career;
import net.exmo.excareerwar.content.CareerHandle;
import net.exmo.excareerwar.inventory.SelectedCareerMenu;
import net.exmo.excareerwar.network.CareerWarModVariables;
import net.exmo.excareerwar.network.SelectedCareerButtonMessage;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import net.minecraft.stats.StatsCounter;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.world.item.Items;

import net.exmo.excareerwar.content.CareerSkill;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;

public class SelectedCareerScreen extends AbstractContainerScreen<SelectedCareerMenu> {
	// 新增滚动相关变量
	private int scrollOffset = 0;
	private int maxScrollOffset = 0;
	private Button scrollUpButton;
	private Button scrollDownButton;
	
	private final static HashMap<String, Object> guistate = SelectedCareerMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private final HashMap<String, String> textstate = new HashMap<>();

	// UI 颜色常量
	private static final int BACKGROUND_COLOR = 0xFFF8F8F8;  // 更浅的背景色
	private static final int PANEL_COLOR_LEFT = 0xFFF0F0F0;  // 左侧面板颜色
	private static final int PANEL_COLOR_MIDDLE = 0xFFF0F0F0; // 中间面板颜色
	private static final int PANEL_COLOR_RIGHT = 0xFFF0F0F0; // 右侧面板颜色
	private static final int HIGHLIGHT_COLOR = 0xFFD0D0D0;   // 更柔和的强调色
	private static final int TEXT_COLOR = 0xFF404040;        // 更深的文本颜色
	private static final int BUTTON_COLOR = 0xFFE0E0E0;      // 按钮基础色
	private static final int BUTTON_HOVER_COLOR = 0xFFD0D0D0; // 按钮悬停色

	// UI 尺寸常量
	private static final int PANEL_PADDING = 10;
	private static final int ELEMENT_SPACING = 8;
	private static final int SKILL_ICON_SIZE = 18;

	private Career selectedCareer;
	private Button selectButton;
	private List<CareerButton> careerButtons = new ArrayList<>();
	private LocalPlayer dummyPlayer;  // 虚拟玩家实体


	public SelectedCareerScreen(SelectedCareerMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		this.imageHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
	}

	@Override
	protected void init() {
		super.init();
		
		// 创建虚拟玩家实体
		dummyPlayer = new LocalPlayer(
			Minecraft.getInstance(), 
			Minecraft.getInstance().level,
			Minecraft.getInstance().getConnection(),
			new StatsCounter() ,
			new ClientRecipeBook() ,
				false ,
				false

		);
		
		// 设置虚拟玩家默认装备（可根据需要自定义）
//		dummyPlayer.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
//		dummyPlayer.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
//		dummyPlayer.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
//		dummyPlayer.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
//		dummyPlayer.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
		


		// 计算布局区域
		int totalWidth = this.width;
		int totalHeight = this.height;

		// 左侧职业列表区域 (1/3宽度)
		int listWidth = totalWidth / 3;
		int listHeight = totalHeight;

		// 中间玩家模型区域 (1/3宽度) - TODO: 实现玩家模型渲染
		int modelWidth = totalWidth / 3;
		int modelHeight = totalHeight;

		// 右侧信息区域 (1/3宽度)
		int infoWidth = totalWidth / 3;
		int infoHeight = totalHeight;

		// 创建职业选择按钮列表
		int buttonY = PANEL_PADDING;
		// 修改：按钮Y坐标考虑滚动偏移
		for (Career career : CareerHandle.registeredCareers) {
			CareerButton button = new CareerButton(
					PANEL_PADDING,
					buttonY - scrollOffset,  // 添加滚动偏移
					listWidth - 2 * PANEL_PADDING,
					40,
					Component.translatable(career.LocalName),
					career,
					b -> {
						selectedCareer = career;
						updateButtonStates();
						career.OnHas(career.LocalName, dummyPlayer);
					}
			);
			button.setTooltip(Tooltip.create(Component.translatable(career.LocalDescription)));
			careerButtons.add(button);
			addRenderableWidget(button);
			buttonY += 40 + ELEMENT_SPACING;
		}
		scrollOffset =0;
		// 计算最大滚动范围
		maxScrollOffset = Math.max(0, buttonY - this.height + PANEL_PADDING);
		
		// 添加上下滚动按钮 (左侧面板右下角) - 修改为自定义按钮类
		int scrollButtonSize = 20;
		int scrollButtonX = listWidth - scrollButtonSize - PANEL_PADDING +21;
		int scrollButtonY = listHeight - scrollButtonSize - PANEL_PADDING;
		
		// 上滚按钮 - 使用自定义ScrollButton
		scrollUpButton = new ScrollButton(
				scrollButtonX, 
				scrollButtonY - scrollButtonSize - 5, 
				scrollButtonSize, 
				scrollButtonSize, 
				Component.literal("↑"), 
				b -> {
					scrollOffset = Math.max(0, scrollOffset - 40);
					updateButtonPositions();
				}
		);
		addRenderableWidget(scrollUpButton);
		
		// 下滚按钮 - 使用自定义ScrollButton
		scrollDownButton = new ScrollButton(
				scrollButtonX, 
				scrollButtonY, 
				scrollButtonSize, 
				scrollButtonSize, 
				Component.literal("↓"), 
				b -> {
					scrollOffset = Math.min(maxScrollOffset, scrollOffset + 40);
					updateButtonPositions();
				}
		);
		addRenderableWidget(scrollDownButton);
		
		// 创建选择按钮 (右下角) - 修改为自定义按钮
		selectButton = new SelectButton(
				totalWidth - infoWidth + PANEL_PADDING,
				totalHeight - 40 - PANEL_PADDING,
				infoWidth - 2 * PANEL_PADDING,
				40,
				Component.translatable("gui.excareerwar.select_career"),
				b -> {
					if (selectedCareer != null) {
						textstate.put("career", selectedCareer.LocalName);
						Excareerwar.PACKET_HANDLER.sendToServer(new SelectedCareerButtonMessage(0, x, y, z, textstate));
						SelectedCareerButtonMessage.handleButtonAction(entity, 0, x, y, z, textstate);

					}
				}
		);
		addRenderableWidget(selectButton);

		updateButtonStates();
	}
	
	// 新增：更新按钮位置方法
	private void updateButtonPositions() {
		int buttonY = PANEL_PADDING;
		for (CareerButton button : careerButtons) {
			button.setY(buttonY - scrollOffset);
			buttonY += 40 + ELEMENT_SPACING;
		}
		// 更新滚动按钮状态
		scrollUpButton.active = (scrollOffset > 0);
		scrollDownButton.active = (scrollOffset < maxScrollOffset);
	}
	
	// 新增：处理鼠标滚轮事件
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		// 只在左侧面板区域响应滚轮
		if (mouseX < this.width / 3) {
			int newOffset = scrollOffset + (int) (-delta * 40);
			scrollOffset = Mth.clamp(newOffset, 0, maxScrollOffset);
			updateButtonPositions();
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, delta);
	}
	
	private void updateButtonStates() {
		selectButton.active = (selectedCareer != null);

		for (CareerButton button : careerButtons) {
			button.setSelected(button.career == selectedCareer);
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		// 渲染全屏背景
		renderBackground(guiGraphics);


		// 渲染UI面板
		renderUIPanels(guiGraphics);

		// 渲染职业信息
		if (selectedCareer != null) {
			renderCareerInfo(guiGraphics);
		}

		// 渲染按钮和文本
		super.render(guiGraphics, mouseX, mouseY, partialTicks);

		// 渲染悬停提示
		renderTooltips(guiGraphics, mouseX, mouseY);
		// 渲染玩家属性tooltip
		//renderPlayerAttributesTooltip(guiGraphics, mouseX, mouseY);
		renderOverlay(guiGraphics);
	}

	public void renderBackground(GuiGraphics guiGraphics) {
		// 填充浅灰色背景
		guiGraphics.fill(0, 0, this.width, this.height, BACKGROUND_COLOR);
	}

	@Override
	protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {

	}

	private void renderUIPanels(GuiGraphics guiGraphics) {
		int totalWidth = this.width;
		int totalHeight = this.height;

		// 左侧职业列表面板 - 使用新颜色
		int listPanelX = 0;
		int listPanelY = 0;
		int listPanelWidth = totalWidth / 3;
		int listPanelHeight = totalHeight;
		guiGraphics.fill(listPanelX, listPanelY,
				listPanelX + listPanelWidth, listPanelY + listPanelHeight,
				PANEL_COLOR_LEFT);

		// 中间玩家模型区域 - 使用新颜色
		int modelPanelX = listPanelWidth;
		int modelPanelY = 0;
		int modelPanelWidth = totalWidth / 3;
		int modelPanelHeight = totalHeight;
		guiGraphics.fill(modelPanelX, modelPanelY,
				modelPanelX + modelPanelWidth, modelPanelY + modelPanelHeight,
				PANEL_COLOR_MIDDLE);
		
		// 渲染虚拟玩家实体
		InventoryScreen.renderEntityInInventoryFollowsAngle(
			guiGraphics, 
			modelPanelX + modelPanelWidth / 2,
                (int) (modelPanelY + modelPanelHeight / 1.2),
			100,
			0f, 
			0, 
			dummyPlayer  // 使用虚拟玩家替代原实体
		);

		// 新增：记录玩家模型区域用于悬停检测
		this.modelRenderArea = new Rectangle(modelPanelX, modelPanelY, modelPanelWidth, modelPanelHeight);
	}
	
	// 新增：玩家模型区域记录
	private Rectangle modelRenderArea;

	// 新增：玩家属性tooltip渲染
	private void renderPlayerAttributesTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		if (modelRenderArea != null && modelRenderArea.contains(mouseX, mouseY)) {
			List<Component> tooltip = new ArrayList<>();
			
			// 获取玩家基础属性
			tooltip.add(Component.translatable("attribute.health", dummyPlayer.getHealth(), dummyPlayer.getMaxHealth()));
			tooltip.add(Component.translatable("attribute.armor", dummyPlayer.getArmorValue()));
			tooltip.add(Component.translatable("attribute.attack_damage", dummyPlayer.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).getValue()));
			tooltip.add(Component.translatable("attribute.attack_speed", dummyPlayer.getAttribute(Attributes.ATTACK_SPEED).getValue()));
			tooltip.add(Component.translatable("attribute.movement_speed", dummyPlayer.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).getValue()));
			
			// 显示职业相关属性（示例）
			if (selectedCareer != null) {
				tooltip.add(Component.translatable("career.proficiency", 
					entity.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY)
						.map(vars -> vars.CareerProficiency.getOrDefault(selectedCareer.LocalName, 0))
						.orElse(0)));
			}
			
			if (!tooltip.isEmpty()) {
				guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
			}
		}
	}

	private static final Map<Long, Map.Entry<Component, ItemStack>> overlayMap = new HashMap<>();
	public static void addOverlay(long time, Component content, ItemStack item) {
		overlayMap.put(time, Map.entry(content, item));
	}
	public  void renderOverlay(GuiGraphics guiGraphics){

		Map<Long, Map.Entry<Component, ItemStack>> toRemove = new HashMap<>();
		overlayMap.forEach((k, v) -> {
			if (System.currentTimeMillis() - k > 3200) {
				toRemove.put(k, v);
			}
		});
		toRemove.forEach((k, v) -> overlayMap.remove(k));
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(0.0F, 0.0F, 2000.0F);
		AtomicInteger line = new AtomicInteger();

		overlayMap.entrySet().stream()
				.sorted(Map.Entry.comparingByKey()) // 按时间戳排序
				.forEach(entry -> {
					ItemStack itemStack = entry.getValue().getValue();
					Component content = entry.getValue().getKey();
					float p281752 = 1 - ((System.currentTimeMillis() - entry.getKey()) / 3200f);
					guiGraphics.setColor(1, 1, 1, p281752);
					guiGraphics.drawCenteredString(font, content,
							this.width / 2,
							(int) (this.height / 5 + (font.lineHeight + 2) * line.get()),
							0xFFFFFF);
					if (!itemStack.isEmpty()) {
						guiGraphics.pose().pushPose();
						guiGraphics.pose().scale(0.2f, 0.2f, 0.2f);
						guiGraphics.renderItem(itemStack,
								(this.width / 2 - 10) * 5,
								(int) (this.height / 5 + (font.lineHeight + 2) * line.get()) * 5);
						guiGraphics.pose().popPose();
					}
					guiGraphics.setColor(1, 1, 1, 1);
					line.getAndIncrement();

				});
		guiGraphics.pose().popPose();
	}
	private void renderCareerInfo(GuiGraphics guiGraphics) {
		int infoPanelX = this.width * 2 / 3;
		int infoPanelY = 0;
		int infoPanelWidth = this.width / 3;
		int infoPanelHeight = this.height;

		int contentX = infoPanelX + PANEL_PADDING;
		int contentY = infoPanelY + PANEL_PADDING;
		int contentWidth = infoPanelWidth - 2 * PANEL_PADDING;

		// 渲染职业名称
		guiGraphics.drawString(font,
				Component.translatable(selectedCareer.LocalName),
				contentX, contentY,
				TEXT_COLOR, false);
		contentY += font.lineHeight + ELEMENT_SPACING;

		// 渲染职业描述
		List<FormattedCharSequence> descriptionLines = Tooltip.splitTooltip(Minecraft.getInstance(), Component.translatable(selectedCareer.LocalDescription));
		for (var line : descriptionLines) {
			guiGraphics.drawString(font, line, contentX, contentY, TEXT_COLOR, false);
			contentY += font.lineHeight + 2;
		}
		contentY += ELEMENT_SPACING;

		// 渲染熟练度
		CareerWarModVariables.PlayerVariables playerVars = entity.getCapability(CareerWarModVariables.PLAYER_VARIABLES_CAPABILITY).orElse(new CareerWarModVariables.PlayerVariables());
		Integer proficiency = playerVars.CareerProficiency.get(selectedCareer.LocalName);
		if (proficiency == null) proficiency = 0;

		guiGraphics.drawString(font,
				Component.translatable("gui.excareerwar.proficiency", proficiency),
				contentX, contentY,
				TEXT_COLOR, false);
		contentY += font.lineHeight + ELEMENT_SPACING;

		// 渲染技能标题
		guiGraphics.drawString(font,
				Component.translatable("gui.excareerwar.skills"),
				contentX, contentY,
				TEXT_COLOR, false);
		contentY += font.lineHeight + ELEMENT_SPACING;

		// 渲染技能图标
		int skillX = contentX;
		for (CareerSkill skill : selectedCareer.Skills) {
			// 渲染技能图标背景
			guiGraphics.fill(skillX, contentY,
					skillX + SKILL_ICON_SIZE, contentY + SKILL_ICON_SIZE,
					BUTTON_COLOR);



			if (skill!=null) {
				if (skill.IconTexture != null) {
					guiGraphics.blit(skill.IconTexture, skillX + SKILL_ICON_SIZE / 2 - 8,
							contentY + (SKILL_ICON_SIZE - font.lineHeight) / 2 - 3, 1, 1, 16, 16, 16, 16);
				} else if (skill.Icon != null)
					guiGraphics.renderItem(skill.Icon.getDefaultInstance(), skillX + SKILL_ICON_SIZE / 2 - 8,
							contentY + (SKILL_ICON_SIZE - font.lineHeight) / 2 - 3);
			}
			skillX += SKILL_ICON_SIZE + ELEMENT_SPACING;

			// 换行检查
			if (skillX + SKILL_ICON_SIZE > contentX + contentWidth) {
				skillX = contentX;
				contentY += SKILL_ICON_SIZE + ELEMENT_SPACING;
			}
		}
	}

	private void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		// 修复：使用与renderCareerInfo相同的坐标计算逻辑
		if (selectedCareer != null) {
			int infoPanelX = this.width * 2 / 3;
			int contentX = infoPanelX + PANEL_PADDING;
			int contentY = PANEL_PADDING;  // 从顶部开始计算
			
			// 渲染职业名称和描述后的Y位置
			contentY += font.lineHeight + ELEMENT_SPACING; // 职业名称
			List<FormattedCharSequence> descriptionLines = Tooltip.splitTooltip(Minecraft.getInstance(), 
					Component.translatable(selectedCareer.LocalDescription));
			contentY += (font.lineHeight + 2) * descriptionLines.size() + ELEMENT_SPACING; // 描述
			contentY += font.lineHeight + ELEMENT_SPACING; // 熟练度
			contentY += font.lineHeight + ELEMENT_SPACING; // 技能标题
			
			// 遍历所有技能计算位置
			int skillX = contentX;
			int skillY = contentY;
			for (CareerSkill skill : selectedCareer.Skills) {
				// 获取技能渲染位置
				int[] pos = getSkillRenderPosition(skillX, skillY, skill);
				int renderX = pos[0];
				int renderY = pos[1];
				
				// 检查鼠标是否悬停
				if (isMouseOverSkill(renderX, renderY, mouseX, mouseY)) {
					List<Component> tooltip = new ArrayList<>();

				if (skill.LocalName != null)	tooltip.add(Component.translatable(skill.LocalName));
				if (skill.CoolDown !=0 && !skill.isPassive)  tooltip.add(Component.translatable("skill_cooldown_text", skill.CoolDown/40));
				else if (skill.isPassive) tooltip.add(Component.translatable("skill_passive_text"));
				if (skill.LocalDescription != null)	tooltip.add(Component.translatable(skill.LocalDescription));

				guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
				}
				
				// 更新下一个技能位置
				skillX = renderX + SKILL_ICON_SIZE + ELEMENT_SPACING;
				if (skillX + SKILL_ICON_SIZE > contentX + (this.width / 3 - 2 * PANEL_PADDING)) {
					skillX = contentX;
					skillY += SKILL_ICON_SIZE + ELEMENT_SPACING;
				}
			}
		}
	}
	
	// 新增：获取技能渲染位置（与renderCareerInfo一致）
	private int[] getSkillRenderPosition(int startX, int startY, CareerSkill skill) {
		return new int[]{startX, startY};
	}
	
	private boolean isMouseOverSkill(int skillX, int skillY, int mouseX, int mouseY) {
		return mouseX >= skillX &&
				mouseX <= skillX + SKILL_ICON_SIZE &&
				mouseY >= skillY &&
				mouseY <= skillY + SKILL_ICON_SIZE;
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		// 背景已在主渲染方法中处理
	}

	// 新增：自定义选择按钮类
	private class SelectButton extends Button {
		public SelectButton(int x, int y, int width, int height, Component message, OnPress onPress) {
			super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
		}

		@Override
		public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			// 使用fill渲染按钮背景
			int color = this.active ? 
				(this.isHovered() ? BUTTON_HOVER_COLOR : BUTTON_COLOR) : 
				0xFF808080; // 禁用状态为灰色
			
			guiGraphics.fill(
				getX(), getY(), 
				getX() + width, getY() + height, 
				color
			);
			
			// 渲染按钮文本（居中）
			int textWidth = font.width(getMessage());
			int textX = getX() + (width - textWidth) / 2;
			int textY = getY() + (height - font.lineHeight) / 2;
			
			guiGraphics.drawString(
				font, 
				getMessage(), 
				textX, textY, 
				TEXT_COLOR, 
				false
			);
		}
	}
	
	// 自定义职业按钮
	private class CareerButton extends Button {
		private final Career career;
		private boolean selected;

		public CareerButton(int x, int y, int width, int height, Component message, Career career, OnPress onPress) {
			super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
			this.career = career;
			this.selected = false;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		@Override
		public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			// 新增：检查按钮是否在可见区域内
			if (getY() + height < 0 || getY() > SelectedCareerScreen.this.height) {
				return; // 不在可见区域，跳过渲染和事件处理
			}
			
			// 绘制按钮背景
			int color = selected ? HIGHLIGHT_COLOR :
					(isHovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR);
			guiGraphics.fill(getX(), getY(), getX() + width, getY() + height, color);

			// 绘制职业图标
			guiGraphics.pose().pushPose();
			guiGraphics.pose().scale(1.5f, 1.5f, 0);
			guiGraphics.renderItem(career.itemIcon.getDefaultInstance(), (int) ((getX() + 8)/1.5), (int) ((getY() + 8)/1.5));
			guiGraphics.pose().popPose();

			// 绘制职业名称
			guiGraphics.drawString(font, getMessage(),
					getX() + 32 + 8,
					getY() + (height - font.lineHeight) / 2,
					TEXT_COLOR, false);
		}
	}
	
	// 新增：自定义滚动按钮类（使用fill渲染）
	private class ScrollButton extends Button {
		public ScrollButton(int x, int y, int width, int height, Component message, OnPress onPress) {
			super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
		}

		@Override
		public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			// 使用fill渲染按钮背景
			int color = this.active ? 
				(this.isHovered() ? BUTTON_HOVER_COLOR : BUTTON_COLOR) : 
				0xFFC0C0C0; // 禁用状态为灰色
			
			guiGraphics.fill(
				getX(), getY(), 
				getX() + width, getY() + height, 
				color
			);
			
			// 渲染按钮文本（居中）
			int textWidth = font.width(getMessage());
			int textX = getX() + (width - textWidth) / 2;
			int textY = getY() + (height - font.lineHeight) / 2;
			
			guiGraphics.drawString(
				font, 
				getMessage(), 
				textX, textY, 
				TEXT_COLOR, 
				false
			);
		}
	}
}
