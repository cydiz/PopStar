<GameFile>
  <PropertyGroup Name="MainMenuScene" Type="Scene" ID="a2ee0952-26b5-49ae-8bf9-4f1d6279b798" Version="3.10.0.0" />
  <Content ctype="GameProjectContent">
    <Content>
      <Animation Duration="220" Speed="1.0000">
        <Timeline ActionTag="-258145048" Property="Scale">
          <ScaleFrame FrameIndex="0" X="0.0100" Y="0.0100">
            <EasingData Type="0" />
          </ScaleFrame>
          <ScaleFrame FrameIndex="95" X="1.0000" Y="1.0000">
            <EasingData Type="0" />
          </ScaleFrame>
          <ScaleFrame FrameIndex="125" X="1.0000" Y="1.0000">
            <EasingData Type="0" />
          </ScaleFrame>
          <ScaleFrame FrameIndex="220" X="0.0100" Y="0.0100">
            <EasingData Type="0" />
          </ScaleFrame>
        </Timeline>
        <Timeline ActionTag="-258145048" Property="RotationSkew">
          <ScaleFrame FrameIndex="0" X="0.0000" Y="0.0000">
            <EasingData Type="0" />
          </ScaleFrame>
        </Timeline>
      </Animation>
      <ObjectData Name="Scene" ctype="GameNodeObjectData">
        <Size X="640.0000" Y="960.0000" />
        <Children>
          <AbstractNodeData Name="sprite_bg" ActionTag="978820226" Tag="175" IconVisible="False" PositionPercentXEnabled="True" PositionPercentYEnabled="True" PercentWidthEnable="True" PercentHeightEnable="True" PercentWidthEnabled="True" PercentHeightEnabled="True" LeftEage="253" RightEage="253" TopEage="337" BottomEage="337" Scale9OriginX="253" Scale9OriginY="337" Scale9Width="262" Scale9Height="350" ctype="ImageViewObjectData">
            <Size X="640.0000" Y="960.0000" />
            <Children>
              <AbstractNodeData Name="Text_1" ActionTag="-1542514290" Tag="18" IconVisible="False" LeftMargin="274.6235" RightMargin="255.3765" TopMargin="17.2232" BottomMargin="922.7768" FontSize="20" LabelText="最高分:1000" ShadowOffsetX="2.0000" ShadowOffsetY="-2.0000" ctype="TextObjectData">
                <Size X="110.0000" Y="20.0000" />
                <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                <Position X="329.6235" Y="932.7768" />
                <Scale ScaleX="1.3000" ScaleY="1.3000" />
                <CColor A="255" R="255" G="255" B="255" />
                <PrePosition X="0.5150" Y="0.9716" />
                <PreSize X="0.1719" Y="0.0208" />
                <OutlineColor A="255" R="255" G="0" B="0" />
                <ShadowColor A="255" R="110" G="110" B="110" />
              </AbstractNodeData>
              <AbstractNodeData Name="light_blue" ActionTag="-258145048" Tag="20" IconVisible="False" LeftMargin="489.8108" RightMargin="-145.8108" TopMargin="110.9681" BottomMargin="553.0319" ctype="SpriteObjectData">
                <Size X="296.0000" Y="296.0000" />
                <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                <Position X="637.8108" Y="701.0319" />
                <Scale ScaleX="0.0100" ScaleY="0.0100" />
                <CColor A="255" R="255" G="255" B="255" />
                <PrePosition X="0.9966" Y="0.7302" />
                <PreSize X="0.4625" Y="0.3083" />
                <FileData Type="Normal" Path="image/MainMenuScene/light_blue.png" Plist="" />
                <BlendFunc Src="1" Dst="771" />
              </AbstractNodeData>
              <AbstractNodeData Name="btn_start_game" ActionTag="-405816856" Tag="32" IconVisible="False" LeftMargin="185.3636" RightMargin="144.6364" TopMargin="497.7006" BottomMargin="381.2994" TouchEnable="True" FontSize="14" LeftEage="15" RightEage="15" TopEage="11" BottomEage="11" Scale9OriginX="15" Scale9OriginY="11" Scale9Width="280" Scale9Height="59" ShadowOffsetX="2.0000" ShadowOffsetY="-2.0000" ctype="ButtonObjectData">
                <Size X="310.0000" Y="81.0000" />
                <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                <Position X="340.3636" Y="421.7994" />
                <Scale ScaleX="1.0000" ScaleY="1.0000" />
                <CColor A="255" R="255" G="255" B="255" />
                <PrePosition X="0.5318" Y="0.4394" />
                <PreSize X="0.4844" Y="0.0844" />
                <TextColor A="255" R="65" G="65" B="70" />
                <DisabledFileData Type="Default" Path="Default/Button_Disable.png" Plist="" />
                <PressedFileData Type="Normal" Path="image/MainMenuScene/menu_start.png" Plist="" />
                <NormalFileData Type="Normal" Path="image/MainMenuScene/menu_start.png" Plist="" />
                <OutlineColor A="255" R="255" G="0" B="0" />
                <ShadowColor A="255" R="110" G="110" B="110" />
              </AbstractNodeData>
              <AbstractNodeData Name="btn_meteor_test" ActionTag="2107456814" Tag="33" IconVisible="False" LeftMargin="185.3636" RightMargin="144.6364" TopMargin="595.5314" BottomMargin="283.4686" TouchEnable="True" FontSize="14" LeftEage="15" RightEage="15" TopEage="11" BottomEage="11" Scale9OriginX="15" Scale9OriginY="11" Scale9Width="280" Scale9Height="59" ShadowOffsetX="2.0000" ShadowOffsetY="-2.0000" ctype="ButtonObjectData">
                <Size X="310.0000" Y="81.0000" />
                <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                <Position X="340.3636" Y="323.9686" />
                <Scale ScaleX="1.0000" ScaleY="1.0000" />
                <CColor A="255" R="255" G="255" B="255" />
                <PrePosition X="0.5318" Y="0.3375" />
                <PreSize X="0.4844" Y="0.0844" />
                <TextColor A="255" R="65" G="65" B="70" />
                <DisabledFileData Type="Default" Path="Default/Button_Disable.png" Plist="" />
                <PressedFileData Type="Normal" Path="image/MainMenuScene/menu_tone.png" Plist="" />
                <NormalFileData Type="Normal" Path="image/MainMenuScene/menu_tone.png" Plist="" />
                <OutlineColor A="255" R="255" G="0" B="0" />
                <ShadowColor A="255" R="110" G="110" B="110" />
              </AbstractNodeData>
              <AbstractNodeData Name="btn_into_shop" ActionTag="-1135504884" Tag="34" IconVisible="False" LeftMargin="185.3636" RightMargin="144.6364" TopMargin="693.3643" BottomMargin="185.6357" TouchEnable="True" FontSize="14" LeftEage="15" RightEage="15" TopEage="11" BottomEage="11" Scale9OriginX="15" Scale9OriginY="11" Scale9Width="280" Scale9Height="59" ShadowOffsetX="2.0000" ShadowOffsetY="-2.0000" ctype="ButtonObjectData">
                <Size X="310.0000" Y="81.0000" />
                <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                <Position X="340.3636" Y="226.1357" />
                <Scale ScaleX="1.0000" ScaleY="1.0000" />
                <CColor A="255" R="255" G="255" B="255" />
                <PrePosition X="0.5318" Y="0.2356" />
                <PreSize X="0.4844" Y="0.0844" />
                <TextColor A="255" R="65" G="65" B="70" />
                <DisabledFileData Type="Default" Path="Default/Button_Disable.png" Plist="" />
                <PressedFileData Type="Normal" Path="image/MainMenuScene/menu_shop.png" Plist="" />
                <NormalFileData Type="Normal" Path="image/MainMenuScene/menu_shop.png" Plist="" />
                <OutlineColor A="255" R="255" G="0" B="0" />
                <ShadowColor A="255" R="110" G="110" B="110" />
              </AbstractNodeData>
              <AbstractNodeData Name="btn_shop2" ActionTag="1794768140" Tag="35" IconVisible="False" LeftMargin="430.8693" RightMargin="26.1307" TopMargin="15.3812" BottomMargin="886.6188" TouchEnable="True" FontSize="14" Scale9Enable="True" LeftEage="15" RightEage="15" TopEage="11" BottomEage="11" Scale9OriginX="15" Scale9OriginY="11" Scale9Width="153" Scale9Height="36" ShadowOffsetX="2.0000" ShadowOffsetY="-2.0000" ctype="ButtonObjectData">
                <Size X="183.0000" Y="58.0000" />
                <Children>
                  <AbstractNodeData Name="Text_2" ActionTag="994485933" Tag="36" IconVisible="False" LeftMargin="88.9583" RightMargin="58.0417" TopMargin="12.3882" BottomMargin="9.6118" FontSize="36" LabelText="22" ShadowOffsetX="2.0000" ShadowOffsetY="-2.0000" ctype="TextObjectData">
                    <Size X="36.0000" Y="36.0000" />
                    <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                    <Position X="106.9583" Y="27.6118" />
                    <Scale ScaleX="1.0000" ScaleY="1.0000" />
                    <CColor A="255" R="26" G="26" B="26" />
                    <PrePosition X="0.5845" Y="0.4761" />
                    <PreSize X="0.1967" Y="0.6207" />
                    <OutlineColor A="255" R="255" G="0" B="0" />
                    <ShadowColor A="255" R="110" G="110" B="110" />
                  </AbstractNodeData>
                </Children>
                <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                <Position X="522.3693" Y="915.6188" />
                <Scale ScaleX="1.0000" ScaleY="1.0000" />
                <CColor A="255" R="255" G="255" B="255" />
                <PrePosition X="0.8162" Y="0.9538" />
                <PreSize X="0.2859" Y="0.0604" />
                <TextColor A="255" R="65" G="65" B="70" />
                <DisabledFileData Type="Default" Path="Default/Button_Disable.png" Plist="" />
                <PressedFileData Type="Normal" Path="image/Common/shop.png" Plist="" />
                <NormalFileData Type="Normal" Path="image/Common/shop.png" Plist="" />
                <OutlineColor A="255" R="255" G="0" B="0" />
                <ShadowColor A="255" R="110" G="110" B="110" />
              </AbstractNodeData>
              <AbstractNodeData Name="btn_big_gift" ActionTag="-1783951086" Tag="37" IconVisible="False" LeftMargin="527.0338" RightMargin="12.9662" TopMargin="472.4819" BottomMargin="372.5181" TouchEnable="True" FontSize="14" Scale9Enable="True" LeftEage="15" RightEage="15" TopEage="11" BottomEage="11" Scale9OriginX="15" Scale9OriginY="11" Scale9Width="70" Scale9Height="93" ShadowOffsetX="2.0000" ShadowOffsetY="-2.0000" ctype="ButtonObjectData">
                <Size X="100.0000" Y="115.0000" />
                <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                <Position X="577.0338" Y="430.0181" />
                <Scale ScaleX="1.0000" ScaleY="1.0000" />
                <CColor A="255" R="255" G="255" B="255" />
                <PrePosition X="0.9016" Y="0.4479" />
                <PreSize X="0.1563" Y="0.1198" />
                <TextColor A="255" R="65" G="65" B="70" />
                <DisabledFileData Type="Default" Path="Default/Button_Disable.png" Plist="" />
                <PressedFileData Type="Normal" Path="image/MainMenuScene/bottom_1.png" Plist="" />
                <NormalFileData Type="Normal" Path="image/MainMenuScene/bottom_1.png" Plist="" />
                <OutlineColor A="255" R="255" G="0" B="0" />
                <ShadowColor A="255" R="110" G="110" B="110" />
              </AbstractNodeData>
              <AbstractNodeData Name="btn_newbie_gift" ActionTag="-364736593" Tag="38" IconVisible="False" LeftMargin="516.5972" RightMargin="17.4028" TopMargin="658.2751" BottomMargin="208.7249" TouchEnable="True" FontSize="14" Scale9Enable="True" LeftEage="15" RightEage="15" TopEage="11" BottomEage="11" Scale9OriginX="15" Scale9OriginY="11" Scale9Width="76" Scale9Height="71" ShadowOffsetX="2.0000" ShadowOffsetY="-2.0000" ctype="ButtonObjectData">
                <Size X="106.0000" Y="93.0000" />
                <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                <Position X="569.5972" Y="255.2249" />
                <Scale ScaleX="1.0000" ScaleY="1.0000" />
                <CColor A="255" R="255" G="255" B="255" />
                <PrePosition X="0.8900" Y="0.2659" />
                <PreSize X="0.1656" Y="0.0969" />
                <TextColor A="255" R="65" G="65" B="70" />
                <DisabledFileData Type="Default" Path="Default/Button_Disable.png" Plist="" />
                <PressedFileData Type="Normal" Path="image/MainMenuScene/item_libao.png" Plist="" />
                <NormalFileData Type="Normal" Path="image/MainMenuScene/item_libao.png" Plist="" />
                <OutlineColor A="255" R="255" G="0" B="0" />
                <ShadowColor A="255" R="110" G="110" B="110" />
              </AbstractNodeData>
              <AbstractNodeData Name="btn_lottery" ActionTag="-798950792" Tag="39" IconVisible="False" LeftMargin="39.2203" RightMargin="496.7797" TopMargin="644.6308" BottomMargin="210.3692" TouchEnable="True" FontSize="14" Scale9Enable="True" LeftEage="15" RightEage="15" TopEage="11" BottomEage="11" Scale9OriginX="15" Scale9OriginY="11" Scale9Width="74" Scale9Height="83" ShadowOffsetX="2.0000" ShadowOffsetY="-2.0000" ctype="ButtonObjectData">
                <Size X="104.0000" Y="105.0000" />
                <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                <Position X="91.2203" Y="262.8692" />
                <Scale ScaleX="1.0000" ScaleY="1.0000" />
                <CColor A="255" R="255" G="255" B="255" />
                <PrePosition X="0.1425" Y="0.2738" />
                <PreSize X="0.1625" Y="0.1094" />
                <TextColor A="255" R="65" G="65" B="70" />
                <DisabledFileData Type="Default" Path="Default/Button_Disable.png" Plist="" />
                <PressedFileData Type="Normal" Path="image/MainMenuScene/icon_iphone.png" Plist="" />
                <NormalFileData Type="Normal" Path="image/MainMenuScene/icon_iphone.png" Plist="" />
                <OutlineColor A="255" R="255" G="0" B="0" />
                <ShadowColor A="255" R="110" G="110" B="110" />
              </AbstractNodeData>
              <AbstractNodeData Name="btn_cdkey" ActionTag="519250386" Tag="40" IconVisible="False" LeftMargin="75.3541" RightMargin="439.6459" TopMargin="802.9917" BottomMargin="43.0083" TouchEnable="True" FontSize="14" Scale9Enable="True" LeftEage="15" RightEage="15" TopEage="11" BottomEage="11" Scale9OriginX="15" Scale9OriginY="11" Scale9Width="95" Scale9Height="92" ShadowOffsetX="2.0000" ShadowOffsetY="-2.0000" ctype="ButtonObjectData">
                <Size X="125.0000" Y="114.0000" />
                <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                <Position X="137.8541" Y="100.0083" />
                <Scale ScaleX="1.0000" ScaleY="1.0000" />
                <CColor A="255" R="255" G="255" B="255" />
                <PrePosition X="0.2154" Y="0.1042" />
                <PreSize X="0.1953" Y="0.1187" />
                <TextColor A="255" R="65" G="65" B="70" />
                <DisabledFileData Type="Default" Path="Default/Button_Disable.png" Plist="" />
                <PressedFileData Type="Normal" Path="image/MainMenuScene/bottom_3.png" Plist="" />
                <NormalFileData Type="Normal" Path="image/MainMenuScene/bottom_3.png" Plist="" />
                <OutlineColor A="255" R="255" G="0" B="0" />
                <ShadowColor A="255" R="110" G="110" B="110" />
              </AbstractNodeData>
              <AbstractNodeData Name="btn_activity" ActionTag="365888653" Tag="41" IconVisible="False" LeftMargin="257.3602" RightMargin="257.6398" TopMargin="802.9917" BottomMargin="43.0083" TouchEnable="True" FontSize="14" Scale9Enable="True" LeftEage="15" RightEage="15" TopEage="11" BottomEage="11" Scale9OriginX="15" Scale9OriginY="11" Scale9Width="95" Scale9Height="92" ShadowOffsetX="2.0000" ShadowOffsetY="-2.0000" ctype="ButtonObjectData">
                <Size X="125.0000" Y="114.0000" />
                <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                <Position X="319.8602" Y="100.0083" />
                <Scale ScaleX="1.0000" ScaleY="1.0000" />
                <CColor A="255" R="255" G="255" B="255" />
                <PrePosition X="0.4998" Y="0.1042" />
                <PreSize X="0.1953" Y="0.1187" />
                <TextColor A="255" R="65" G="65" B="70" />
                <DisabledFileData Type="Default" Path="Default/Button_Disable.png" Plist="" />
                <PressedFileData Type="Normal" Path="image/MainMenuScene/bottom_4.png" Plist="" />
                <NormalFileData Type="Normal" Path="image/MainMenuScene/bottom_4.png" Plist="" />
                <OutlineColor A="255" R="255" G="0" B="0" />
                <ShadowColor A="255" R="110" G="110" B="110" />
              </AbstractNodeData>
              <AbstractNodeData Name="btn_setting" ActionTag="-1131122163" Tag="42" IconVisible="False" LeftMargin="439.3633" RightMargin="75.6367" TopMargin="799.2717" BottomMargin="46.7283" TouchEnable="True" FontSize="14" Scale9Enable="True" LeftEage="15" RightEage="15" TopEage="11" BottomEage="11" Scale9OriginX="15" Scale9OriginY="11" Scale9Width="95" Scale9Height="92" ShadowOffsetX="2.0000" ShadowOffsetY="-2.0000" ctype="ButtonObjectData">
                <Size X="125.0000" Y="114.0000" />
                <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
                <Position X="501.8633" Y="103.7283" />
                <Scale ScaleX="1.0000" ScaleY="1.0000" />
                <CColor A="255" R="255" G="255" B="255" />
                <PrePosition X="0.7842" Y="0.1081" />
                <PreSize X="0.1953" Y="0.1187" />
                <TextColor A="255" R="65" G="65" B="70" />
                <DisabledFileData Type="Default" Path="Default/Button_Disable.png" Plist="" />
                <PressedFileData Type="Normal" Path="image/MainMenuScene/bottom_5.png" Plist="" />
                <NormalFileData Type="Normal" Path="image/MainMenuScene/bottom_5.png" Plist="" />
                <OutlineColor A="255" R="255" G="0" B="0" />
                <ShadowColor A="255" R="110" G="110" B="110" />
              </AbstractNodeData>
            </Children>
            <AnchorPoint ScaleX="0.5000" ScaleY="0.5000" />
            <Position X="320.0000" Y="480.0000" />
            <Scale ScaleX="1.0000" ScaleY="1.0000" />
            <CColor A="255" R="255" G="255" B="255" />
            <PrePosition X="0.5000" Y="0.5000" />
            <PreSize X="1.0000" Y="1.0000" />
            <FileData Type="Normal" Path="image/Common/background_layer0.png" Plist="" />
          </AbstractNodeData>
          <AbstractNodeData Name="FileNode_4" ActionTag="676854824" Tag="378" IconVisible="True" LeftMargin="347.6349" RightMargin="292.3651" TopMargin="287.4993" BottomMargin="672.5007" StretchWidthEnable="False" StretchHeightEnable="False" InnerActionSpeed="1.0000" CustomSizeEnabled="False" ctype="ProjectNodeObjectData">
            <Size X="0.0000" Y="0.0000" />
            <AnchorPoint />
            <Position X="347.6349" Y="672.5007" />
            <Scale ScaleX="1.0000" ScaleY="1.0000" />
            <CColor A="255" R="255" G="255" B="255" />
            <PrePosition X="0.5432" Y="0.7005" />
            <PreSize X="0.0000" Y="0.0000" />
            <FileData Type="Normal" Path="image/MainMenuScene/bg_light.csd" Plist="" />
          </AbstractNodeData>
        </Children>
      </ObjectData>
    </Content>
  </Content>
</GameFile>